package com.firespread.controller;

import com.alibaba.fastjson.JSON;
import com.firespread.entity.FireBoundry;
import com.firespread.entity.LatAndLogEntity;
import com.firespread.service.FireBoundryService;
import com.firespread.service.WeatherPointService;
import com.firespread.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @description: 接口类
 * @author: DelucaWu
 * @createDate: 2022/5/10 20:19
 */
@RestController
@Api(tags = {"林火蔓延接口"})
public class FireBoundryController {

    @Autowired
    OperateShap operateShap;
    @Autowired
    UTMToLatLon utmToLatLon;
    @Autowired
    FireBoundryService fireBoundryService;
    @Autowired
    WeatherPointService weatherPointService;

    private String shp_path = "Data//point_180.shp";
    private String fdinput_path = "Data//point_fd.input";
    private String sninput_path = "Data//point_sn.input";
    private String result_path = "Data//fireData//_Perimeters.shp";

    @RequestMapping(value = "/fireBoundry/POST/startAnalysis",method= RequestMethod.POST)
    @ApiOperation("启动蔓延分析")
    public Map<Integer,List<Double>> startAnalysis(Double lon,Double lat,Integer userid) throws IOException {

        String command;
        File fdinputFile = new File(fdinput_path);
        File sninputFile = new File(sninput_path);

        Map<Integer,List<Double>> map = new HashMap<>();

        String zh = weatherPointService.findweatherPoint(lon,lat);
//        System.out.println(zh);

        if(lon<120.4535&&lon>119.9161&&lat<27.4514&&lat>26.9138) {

            replaceinput replaceinput=new replaceinput();
            replaceinput.rewritefd(fdinput_path,zh);
            if(!fdinputFile.exists()){
                List<Double> ld=new ArrayList<>();
                ld.add(1.0);
                map.put(1,ld);
                return map;
            }
            command = "cmd /c Shuyan.exe Commandfd.txt";

        } else if(lon<119.7349&&lon>119.2329&&lat<27.681&&lat>27.169){

            replaceinput replaceinput=new replaceinput();
            replaceinput.rewritesn(sninput_path,zh);
            if(!sninputFile.exists()){
                List<Double> ld=new ArrayList<>();
                ld.add(1.0);
                map.put(1,ld);
                return map;
            }
            command = "cmd /c Shuyan.exe Commandsn.txt";
        }
        else{
            List<Double> ld=new ArrayList<>();
            ld.add(2.0);
            map.put(1,ld);
            return map;
        }

        LatLonToUTM latLonToUTM = new LatLonToUTM();
        LatAndLogEntity latAndLog = latLonToUTM.LatLonToUTMXY(lat,lon,50);

        File resultFile = new File(result_path);

        File shpFile = new File(shp_path);

        if(!shpFile.exists()){
            shpFile.createNewFile();
        }
        if(resultFile.exists()){
            resultFile.delete();
        }
        OperateShap createShap = new OperateShap();
        createShap.createShp(shp_path,latAndLog.getX(),latAndLog.getY());

        String libPath = "./Data";

        File f = new File(libPath);

        Process p = null;
        p = Runtime.getRuntime().exec(command, null, new File(libPath));

        LineNumberReader br = new LineNumberReader(new InputStreamReader(p.getInputStream(), "GBK"));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        boolean status = sb.toString().contains("Output");
        if(!status){
            List<Double> ld=new ArrayList<>();
            ld.add(3.0);
            map.put(1,ld);
            return map;
        }
        map = analysisData();
        insertinfo(lon,lat,userid);
        return map;
    }

//    @ApiOperation("获取蔓延分析结果")
//    @RequestMapping(value = "/fireBoundry/GET/analysisData",method=RequestMethod.GET)
    public Map<Integer,List<Double>> analysisData() {

        File file = new File(result_path);
        DecimalFormat d=new DecimalFormat("#.000");
        Map<Integer,List<Double>> map = new HashMap<>();
        if(!file.exists()){
            List<Double> ld=new ArrayList<>();
            ld.add(3.0);
            map.put(1,ld);
            return map;
        }

        List list = operateShap.readShp(result_path);
        for(int i=0;i<list.size();i++){
            int k=i+1;
            List<Double> coordsArray=new ArrayList<>();
            String[] arr = list.get(i).toString()
            .replace("SimpleFeatureImpl.Attribute: the_geom<MultiLineString id=_Perimeters."+k+">=MULTILINESTRING","")
            .replace("(","")
            .replace(")","").replace("[","").
            replace("]","").split(",");

            for(int j=0;j<arr.length;j++){
                String[] arr1 = arr[j].split(" ");
                LatAndLogEntity latAndLog = utmToLatLon.UTMXYToLatLon(Double.parseDouble(arr1[1]),Double.parseDouble(arr1[2]),50,false);

                coordsArray.add(latAndLog.getLog());
                coordsArray.add(latAndLog.getLat());
            }
            map.put(k,coordsArray);
        }
        return map;
    }

//    @ApiOperation("导入数据库")
//    @RequestMapping(value = "/fireBoundry/INSERT/insertBoundry",method=RequestMethod.POST)
    public int insertinfo(Double lon, Double lat, Integer userid) {

        File file = new File(result_path);
        if(!file.exists()){
            return 0;
        }

        String cc = "";
        DecimalFormat d=new DecimalFormat("#.000");

        List list = operateShap.readShp(result_path);
        for(int i=0;i<list.size();i++){
            int k=i+1;
            String ll = "";
            List<Double> coordsArray=new ArrayList<>();
            String[] arr = list.get(i).toString()
                    .replace("SimpleFeatureImpl.Attribute: the_geom<MultiLineString id=_Perimeters."+k+">=MULTILINESTRING","")
                    .replace("(","")
                    .replace(")","").replace("[","").
                    replace("]","").split(",");

            for(int j=0;j<arr.length;j++){
                String[] arr1 = arr[j].split(" ");
                LatAndLogEntity latAndLog = utmToLatLon.UTMXYToLatLon(Double.parseDouble(arr1[1]), Double.parseDouble(arr1[2]), 50, false);
                ll = ll+d.format(latAndLog.getLog())+" "+d.format(latAndLog.getLat())+",";
            }
            cc=cc+ll+";";
        }

        return fireBoundryService.insertinfo(new FireBoundry(lon,lat,cc,userid));
    }

    @ApiOperation("获取某一边界历史数据")
    @RequestMapping (value = "/fireBoundry/GET/analysisHistoryBylonlat",method=RequestMethod.GET)
    public Map<Integer,List<Double>> analysisDataHistory(
            @RequestParam(value = "lon") Double lon,
            @RequestParam(value = "lat") Double lat,
            @RequestParam(value = "userid") Integer userid
    ) {

        Map<Integer,List<Double>> map = new HashMap<>();

        String list = fireBoundryService.findBoundry(lon,lat,userid);

        String[] arr = list.split(";");

        for(int j=0;j<arr.length;j++){
            int k=j+1;
            List<Double> coordsArray=new ArrayList<>();
            String[] arr1 = arr[j].split(",");
            for(int r=0;r<arr1.length;r++) {
                String[] arr2 = arr1[r].split(" ");
                coordsArray.add(Double.parseDouble(arr2[0]));
                coordsArray.add(Double.parseDouble(arr2[1]));
            }
            map.put(k,coordsArray);
        }

        return map;
    }

    @ApiOperation("获取所有边界历史数据")
    @RequestMapping (value = "/fireBoundry/GET/allAnalysisHistory",method=RequestMethod.GET)
    public String allAnalysisData(@RequestParam(value = "userid") Integer userid) {
        String BoundryList = JSON.toJSONString(fireBoundryService.findAllBoundry(userid));
        return BoundryList;
    }

    @Test
    public void test1(){
        OperateShap operateShap = new OperateShap();
        UTMToLatLon utmToLatLon = new UTMToLatLon();
        String result_path = "Data//fireData//_Perimeters.shp";
        List list = operateShap.readShp(result_path);
        String cc = "";
        for(int i=0;i<list.size();i++){
            int k=i+1;
            String ll = "";
            List<Double> coordsArray=new ArrayList<>();
            String[] arr = list.get(i).toString()
                    .replace("SimpleFeatureImpl.Attribute: the_geom<MultiLineString id=_Perimeters."+k+">=MULTILINESTRING","")
                    .replace("(","")
                    .replace(")","").replace("[","").
                    replace("]","").split(",");

            for(int j=0;j<arr.length;j++){
                String[] arr1 = arr[j].split(" ");
                LatAndLogEntity latAndLog = utmToLatLon.UTMXYToLatLon(Double.parseDouble(arr1[1]), Double.parseDouble(arr1[2]), 50, false);
                ll = ll+latAndLog.getLog()+" "+latAndLog.getLat()+",";
            }
            cc=cc+ll+";";
        }
        System.out.println(cc);
    }

}
