package com.livraison.demo.application.service.optimizer;

import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.entity.Tour;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("clarke")
public  class NearestNeighborOptimizer implements TourOptimizer{


    public NearestNeighborOptimizer() {
    }


    public List<Map<String, Object>> calculateOptimalTour(Tour tour){
        Map<String ,double[]> gpsPointsMap = new LinkedHashMap<>();

        //Stock Warehouse in map
        gpsPointsMap.put(tour.getWarehouse().getName(), new double[]{
                tour.getWarehouse().getLatitude(),
                tour.getWarehouse().getLongitude()
        });

        //Stock Delivery in map  with id

        int i = 1;
        for (Delivery delivery : tour.getDeliveries()) {
            gpsPointsMap.put( "Delivery " + i, new double[]{
                    delivery.getLatitude(),
                    delivery.getLongitude(),
                    delivery.getVolumeM3(),
                    delivery.getWeightKg(),
            });
            i++;
        }

        List<Map.Entry<String, double[]>> entries = new ArrayList<>(gpsPointsMap.entrySet());

        int n = entries.size();

        //pour stock les Distance enter  pont A ou b
        int[][] distances = new int [n][n];

        //calcul Distance enter  pont A ou b
        for (int d = 0; d < n; d++) {
            for (int j = 0; j < n; j++) {
                double[] pointA = entries.get(d).getValue();
                double[] pointB = entries.get(j).getValue();
                distances[d][j] = (int) Math.round(haversine(
                        pointA[0], pointA[1],
                        pointB[0], pointB[1]
                ));
            }
        }
        int TotalDistance = getTotalDistance(distances ,n);
        int[] route = nearestNeighborTSP(distances);


        Map<String, Object> tourMap = new LinkedHashMap<>();
        tourMap.put("tourId", tour.getId());
        tourMap.put("optimizationMethod", "NEAREST");
        tourMap.put("totalDistance", TotalDistance);

        List<Map<String, Object>> orderedDeliveries = new ArrayList<>();

        for (int j = 1; j < route.length - 1; j++) {
            int idx = route[j];
            if (idx > 0) {
                Map.Entry<String, double[]> entry = entries.get(idx);
                Map<String, Object> delivery = new LinkedHashMap<>();
                delivery.put("id", idx);
                delivery.put("position", j);
                delivery.put("latitude", entry.getValue()[0]);
                delivery.put("longitude", entry.getValue()[1]);
                delivery.put("VolumeM3", entry.getValue()[2]);
                delivery.put("WeightKg", entry.getValue()[3]);


                orderedDeliveries.add(delivery);
            }
        }

        tourMap.put("orderedDeliveries", orderedDeliveries);



        return Collections.singletonList(tourMap);

    }



    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
    final double R = 6371;
    double dLat =Math.toRadians(lat2 - lat1);
    double dLon =Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat/2)*Math.sin(dLat/2)+
            Math.cos(Math.toRadians(lat1))*
                    Math.cos(Math.toRadians(lat2))*
                    Math.sin(dLon/2) *Math.sin(dLon/2);
            double c =  2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
            return R * c;
    }

    private static int[] nearestNeighborTSP(int[][] distances) {
        int n = distances.length;
        boolean[] visited = new boolean[n];
        int[] route = new int[n+2];
        route[1] = 0;
        visited[0] = true;
        int totalDistance = 0 ;
        int count = 1;
        for(int setp = 1 ; setp < n ;setp++){
            int last = route[count];
            int nearest = -1;
            int minDist = Integer.MAX_VALUE;
            for(int i = 0 ; i < n ; i++){
        if(!visited[i] && distances[last][i] < minDist){
            minDist = distances[last][i];
            nearest = i ;
        }
            }
            route[++count] = nearest;
            visited[nearest] = true;
            totalDistance += minDist;
        }
        totalDistance += distances[route[count]][0];
        route[++count] = 0;
        route[0] = count - 1;
        route[count] = totalDistance;
        return route;

    }

    public int getTotalDistance(int[][] distances, int n) {
        List<Integer> orderList = new ArrayList<>();
        for(int i = 0 ; i<n ;i++){
            orderList.add(i);
        }
        int[] tourOrder = orderList.stream().mapToInt(Integer::intValue).toArray();
        int toutal =0;
        for(int i = 0 ; i<tourOrder.length -1 ;i++){
            int from = tourOrder[i];
            int to = tourOrder[i+1];
            toutal = distances[from][to];
        }

        return toutal;
    }

}
