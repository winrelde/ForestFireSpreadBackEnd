package com.firespread.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.*;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @description: 矢量操作类
 * @author: DelucaWu
 * @createDate: 2022/5/10 20:19
 */
@Component
public class OperateShap {

    private double PI = 3.14159265358979;

    public List readShp(String filepath){
        List list = new ArrayList();
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        try {
            ShapefileDataStore sds = (ShapefileDataStore)dataStoreFactory.createDataStore(new File(filepath).toURI().toURL());
            SimpleFeatureSource featureSource = sds.getFeatureSource();
            SimpleFeatureIterator itertor = featureSource.getFeatures().features();

            while(itertor.hasNext()) {
                SimpleFeature feature = itertor.next();
                Iterator<Property> it = feature.getProperties().iterator();
                list.add(it.next().toString());
            }
            itertor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean createShp(String filePath,double x0, double y0) {

        try {
            int n =40;
            double r = 30;
            double a=2*PI/n;

            Coordinate[] coords = new Coordinate[n+1];
            for(int i = 0 ; i < n; ++i)
            {
                Coordinate coordinate = new Coordinate(r*cos(a*i)+x0, r*sin(a*i)+y0);
                coords[i] = coordinate;
            }
            coords[n] = new Coordinate(r*cos(0)+x0, r*sin(0)+y0);
            File file = new File(filePath);
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
            SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
            tb.setSRS("EPSG:32650");
            tb.setName("shapefile");
            tb.add("the_geom", Polygon.class);
            ds.createSchema(tb.buildFeatureType());
            ds.setCharset(Charset.forName("GBK"));

            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            SimpleFeature feature = writer.next();

            GeometryFactory geometryFactory = new GeometryFactory();

            LinearRing ring = geometryFactory.createLinearRing(coords);
            LinearRing holes[] = null;
            feature.setAttribute("the_geom", geometryFactory.createPolygon(ring, holes));
            writer.write();
            writer.close();
            ds.dispose();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
