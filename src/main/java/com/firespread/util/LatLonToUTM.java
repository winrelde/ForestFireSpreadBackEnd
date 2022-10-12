package com.firespread.util;

import com.firespread.entity.LatAndLogEntity;
import org.springframework.stereotype.Component;

import static java.lang.Math.*;

/**
 * @description: 经纬度与墨卡托投影转换
 * @author: DelucaWu
 * @createDate: 2022/5/10 20:19
 */
@Component
public class LatLonToUTM {

    private double PI = 3.14159265358979;
    double UTMScaleFactor = 0.9996;

    double sm_a = 6378137.0;
    double sm_b = 6356752.314;



    public LatAndLogEntity LatLonToUTMXY(double lat, double lon, int zone)
    {

        LatAndLogEntity latAndLog = MapLatLonToXY(DegToRad(lat), DegToRad(lon), UTMCentralMeridian(zone));
        double x = latAndLog.getxMedium();
        double y = latAndLog.getyMedium();

        x = x * UTMScaleFactor + 500000.0;
        y = y * UTMScaleFactor;
        if (y < 0.0){
            y += 10000000.0;
        }
        latAndLog.setX(x);
        latAndLog.setY(y);
        return latAndLog;
    }

    public LatAndLogEntity MapLatLonToXY(double phi, double lambda, double lambda0)
    {
        double N, nu2, ep2, t, t2, l;
        double l3coef, l4coef, l5coef, l6coef, l7coef, l8coef;
        double tmp;

        LatAndLogEntity latAndLog = new LatAndLogEntity();

        /* Precalculate ep2 */
        ep2 = (pow(sm_a, 2.0) - pow(sm_b, 2.0)) / pow(sm_b, 2.0);

        /* Precalculate nu2 */
        nu2 = ep2 * pow(cos(phi), 2.0);

        /* Precalculate N */
        N = pow(sm_a, 2.0) / (sm_b * sqrt(1 + nu2));

        /* Precalculate t */
        t = tan(phi);
        t2 = t * t;
        tmp = (t2 * t2 * t2) - pow(t, 6.0);

        /* Precalculate l */
        l = lambda - lambda0;

        l3coef = 1.0 - t2 + nu2;

        l4coef = 5.0 - t2 + 9 * nu2 + 4.0 * (nu2 * nu2);

        l5coef = 5.0 - 18.0 * t2 + (t2 * t2) + 14.0 * nu2 - 58.0 * t2 * nu2;

        l6coef = 61.0 - 58.0 * t2 + (t2 * t2) + 270.0 * nu2 - 330.0 * t2 * nu2;

        l7coef = 61.0 - 479.0 * t2 + 179.0 * (t2 * t2) - (t2 * t2 * t2);

        l8coef = 1385.0 - 3111.0 * t2 + 543.0 * (t2 * t2) - (t2 * t2 * t2);

        double x = N * cos(phi) * l + (N / 6.0 * pow(cos(phi), 3.0) * l3coef * pow(l, 3.0))
                + (N / 120.0 * pow(cos(phi), 5.0) * l5coef * pow(l, 5.0))
                + (N / 5040.0 * pow(cos(phi), 7.0) * l7coef * pow(l, 7.0));

        double y = ArcLengthOfMeridian(phi)
                + (t / 2.0 * N * pow(cos(phi), 2.0) * pow(l, 2.0))
                + (t / 24.0 * N * pow(cos(phi), 4.0) * l4coef * pow(l, 4.0))
                + (t / 720.0 * N * pow(cos(phi), 6.0) * l6coef * pow(l, 6.0))
                + (t / 40320.0 * N * pow(cos(phi), 8.0) * l8coef * pow(l, 8.0));
        latAndLog.setxMedium(x);
        latAndLog.setyMedium(y);
        return latAndLog;
    }

    private double UTMCentralMeridian(int zone)
    {
        return DegToRad(-183.0 + (zone * 6.0));
    }


    private double DegToRad(double deg)
    {
        return (deg / 180.0 * PI);
    }

    double ArcLengthOfMeridian(double phi)
    {
        double alpha, beta, gamma, delta, epsilon, n;
        double result;

        /* Precalculate n */
        n = (sm_a - sm_b) / (sm_a + sm_b);

        /* Precalculate alpha */
        alpha = ((sm_a + sm_b) / 2.0) * (1.0 + (pow(n, 2.0) / 4.0) + (pow(n, 4.0) / 64.0));

        /* Precalculate beta */
        beta = (-3.0 * n / 2.0) + (9.0 * pow(n, 3.0) / 16.0) + (-3.0 * pow(n, 5.0) / 32.0);

        /* Precalculate gamma */
        gamma = (15.0 * pow(n, 2.0) / 16.0) + (-15.0 * pow(n, 4.0) / 32.0);

        /* Precalculate delta */
        delta = (-35.0 * pow(n, 3.0) / 48.0) + (105.0 * pow(n, 5.0) / 256.0);

        /* Precalculate epsilon */
        epsilon = (315.0 * pow(n, 4.0) / 512.0);

        /* Now calculate the sum of the series and return */
        result = alpha * (phi + (beta * sin(2.0 * phi)) + (gamma * sin(4.0 * phi)) + (delta * sin(6.0 * phi)) + (epsilon * sin(8.0 * phi)));

        return result;
    }

}
