package com.livraison.demo.application.service.optimizer;

import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.entity.Tour;
import com.livraison.demo.domain.entity.Warehouse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
//@ConditionalOnProperty(name = "optimizer.type", havingValue = "clarke", matchIfMissing = true)
public class ClarkeWrightOptimizer implements TourOptimizer {

    @Override
    public List<Map<String, Object>> calculateOptimalTour(Tour tour) {
        List<Delivery> deliveries = new ArrayList<>(tour.getDeliveries());
        Warehouse warehouse = tour.getWarehouse();

        // Calculer toutes les distances Warehouse Delivery et Delivery Delivery
        Map<String, Double> distanceMap = new HashMap<>();
        for (Delivery d : deliveries) {
            distanceMap.put("WH-" + d.getId(),
                    distance(warehouse.getLatitude(), warehouse.getLongitude(), d.getLatitude(), d.getLongitude()));
        }
        for (int i = 0; i < deliveries.size(); i++) {
            for (int j = i + 1; j < deliveries.size(); j++) {
                Delivery a = deliveries.get(i);
                Delivery b = deliveries.get(j);
                distanceMap.put(a.getId() + "-" + b.getId(),
                        distance(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude()));
            }
        }
        int n = deliveries.size();
        // Calculer les économies pour chaque paire
        List<Economie> economies = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < deliveries.size(); j++) {
                Delivery a = deliveries.get(i);
                Delivery b = deliveries.get(j);
                double economie = distanceMap.get("WH-" + a.getId())
                        + distanceMap.get("WH-" + b.getId())
                        - distanceMap.get(a.getId() + "-" + b.getId());
                economies.add(new Economie(a, b, economie));
            }
        }

        // Trier par économie décroissante
        economies.sort((e1, e2) -> Double.compare(e2.value, e1.value));

        // Initialiser Tours individuels
        Map<Long, List<Delivery>> tourMap = new HashMap<>();
        for (Delivery d : deliveries) {
            tourMap.put(d.getId(), new ArrayList<>(Collections.singletonList(d)));
        }

        // Fusionner les Tours selon les économies
        for (Economie e : economies) {
            List<Delivery> tourA = tourMap.get(e.a.getId());
            List<Delivery> tourB = tourMap.get(e.b.getId());

            if (tourA != tourB) {
                tourA.addAll(tourB);
                for (Delivery d : tourB) {
                    tourMap.put(d.getId(), tourA);
                }
            }
        }

        // Récupérer le Tour final
        List<Delivery> finalTour = new ArrayList<>(tourMap.values().iterator().next());

        List<String> tourOrder = new ArrayList<>();
        for (Delivery d : finalTour) {
            tourOrder.add(String.valueOf(d.getId()));
        }
        double totalDistance = getTotalDistance(distanceMap, tourOrder);

        List<Map<String, Object>> orderedDeliveries = new ArrayList<>();
        int position = 1;
        for (Delivery d : finalTour) {
            Map<String, Object> deliveryMap = new LinkedHashMap<>();
            deliveryMap.put("id", d.getId());
            deliveryMap.put("position", position++);
            deliveryMap.put("latitude", d.getLatitude());
            deliveryMap.put("longitude", d.getLongitude());
            deliveryMap.put("weightKg", d.getWeightKg());
            deliveryMap.put("volumeM3", d.getVolumeM3());

            orderedDeliveries.add(deliveryMap);
        }

        Map<String, Object> tourResult = new LinkedHashMap<>();
        tourResult.put("tourId", tour.getId());
        tourResult.put("optimizationMethod", "CLARKE");
        tourResult.put("totalDistance", totalDistance);
        tourResult.put("orderedDeliveries", orderedDeliveries);

        return Collections.singletonList(tourResult);
    }

        private static class Economie {
        Delivery a;
        Delivery b;
        double value;

        public Economie(Delivery a, Delivery b, double value) {
            this.a = a;
            this.b = b;
            this.value = value;
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double dx = lat1 - lat2;
        double dy = lon1 - lon2;
        return Math.sqrt(dx * dx + dy * dy);
    }


    public double getTotalDistance(Map<String, Double> distances, List<String> tourOrder) {
        List<Integer> orderList = new ArrayList<>();
        double total = 0.0;
        String key1 = null;
        String key2 = null;

        if(!tourOrder.isEmpty()){
            String firstDelivery = tourOrder.get(0);
            String whKey = "WH-" + firstDelivery;
            if (distances.containsKey(whKey)) {
                total += distances.get(whKey);
            }
        }
        for (int i = 0; i < tourOrder.size() - 1; i++) {
            String from = tourOrder.get(i);
            String to = tourOrder.get(i + 1);

            key1 = from + "-" + to;
            key2 = to + "-" + from;


        if (distances.containsKey(key1)) total += distances.get(key1);
        if (distances.containsKey(key2)) total += distances.get(key2);
        }


        return  total;
    }
}
