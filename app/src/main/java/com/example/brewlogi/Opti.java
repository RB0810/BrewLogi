package com.example.brewlogi;



import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Opti{
    public static Integer result;

        private static Map<String, Map<String, Map<String, Integer>>> total = new HashMap<>();

        static {
            total.put("Total Stock", Map.of(
                    "Lager", Map.of("Cans_distributed", 2800, "Cans_Left", 4400),
                    "Original", Map.of("Cans_distributed", 3200, "Cans_Left", 5300),
                    "Premium", Map.of("Cans_distributed", 2000, "Cans_Left", 4600)
            ));

            total.put("Stall A", Map.of(
                    "Lager", Map.of("Cans_distributed", 207, "Cans_Left", 493),
                    "Original", Map.of("Cans_distributed", 217, "Cans_Left", 583),
                    "Premium", Map.of("Cans_distributed", 206, "Cans_Left", 294)
            ));

            total.put("Stall B", Map.of(
                    "Lager", Map.of("Cans_distributed", 207, "Cans_Left", 493),
                    "Original", Map.of("Cans_distributed", 229, "Cans_Left", 571),
                    "Premium", Map.of("Cans_distributed", 195, "Cans_Left", 305)
            ));

            total.put("Stall C", Map.of(
                    "Lager", Map.of("Cans_distributed", 206, "Cans_Left", 494),
                    "Original", Map.of("Cans_distributed", 221, "Cans_Left", 579),
                    "Premium", Map.of("Cans_distributed", 202, "Cans_Left", 298)
            ));

            total.put("Stall D", Map.of(
                    "Lager", Map.of("Cans_distributed", 214, "Cans_Left", 486),
                    "Original", Map.of("Cans_distributed", 233, "Cans_Left", 567),
                    "Premium", Map.of("Cans_distributed", 231, "Cans_Left", 269)
            ));
        }


    private static int getRandomInt(int min, int max) {
            Random random = new Random();
            return random.nextInt(max - min + 1) + min;
        }

        private static Map<String, Map<String, Map<String, Integer>>> generateSales() {
            Map<String, Map<String, Map<String, Integer>>> sales = new HashMap<>();

            sales.put("Stall A", Map.of(
                    "Lager", Map.of(
                            "hour_1", getRandomInt(100, 130),
                            "hour_2", getRandomInt(0, 10),
                            "hour_3", getRandomInt(60, 85),
                            "hour_4", getRandomInt(0, 7),
                            "hour_5", getRandomInt(0, 5)
                    ),
                    "Original", Map.of(
                            "hour_1", getRandomInt(150, 180),
                            "hour_2", getRandomInt(0, 14),
                            "hour_3", getRandomInt(70, 90),
                            "hour_4", getRandomInt(0, 9),
                            "hour_5", getRandomInt(0, 7)
                    ),
                    "Premium", Map.of(
                            "hour_1", getRandomInt(80, 90),
                            "hour_2", getRandomInt(0, 9),
                            "hour_3", getRandomInt(50, 65),
                            "hour_4", getRandomInt(0, 8),
                            "hour_5", getRandomInt(0, 4)
                    )
            ));

            sales.put("Stall B", Map.of(
                    "Lager", Map.of(
                            "hour_1", getRandomInt(180, 200),
                            "hour_2", getRandomInt(0, 14),
                            "hour_3", getRandomInt(80, 95),
                            "hour_4", getRandomInt(0, 14),
                            "hour_5", getRandomInt(0, 10)
                    ),
                    "Original", Map.of(
                            "hour_1", getRandomInt(220, 250),
                            "hour_2", getRandomInt(0, 20),
                            "hour_3", getRandomInt(90, 102),
                            "hour_4", getRandomInt(0, 15),
                            "hour_5", getRandomInt(0, 13)
                    ),
                    "Premium", Map.of(
                            "hour_1", getRandomInt(130, 160),
                            "hour_2", getRandomInt(0, 13),
                            "hour_3", getRandomInt(60, 70),
                            "hour_4", getRandomInt(0, 12),
                            "hour_5", getRandomInt(0, 9)
                    )
            ));

            sales.put("Stall C", Map.of(
                    "Lager", Map.of(
                            "hour_1", getRandomInt(100, 140),
                            "hour_2", getRandomInt(0, 9),
                            "hour_3", getRandomInt(50, 55),
                            "hour_4", getRandomInt(0, 7),
                            "hour_5", getRandomInt(0, 6)
                    ),
                    "Original", Map.of(
                            "hour_1", getRandomInt(120, 140),
                            "hour_2", getRandomInt(0, 10),
                            "hour_3", getRandomInt(60, 70),
                            "hour_4", getRandomInt(0, 7),
                            "hour_5", getRandomInt(0, 7)
                    ),
                    "Premium", Map.of(
                            "hour_1", getRandomInt(70, 90),
                            "hour_2", getRandomInt(0, 7),
                            "hour_3", getRandomInt(40, 52),
                            "hour_4", getRandomInt(0, 6),
                            "hour_5", getRandomInt(0, 5)
                    )
            ));

            sales.put("Stall D", Map.of(
                    "Lager", Map.of(
                            "hour_1", getRandomInt(110, 130),
                            "hour_2", getRandomInt(0, 10),
                            "hour_3", getRandomInt(70, 80),
                            "hour_4", getRandomInt(0, 8),
                            "hour_5", getRandomInt(0, 7)
                    ),
                    "Original", Map.of(
                            "hour_1", getRandomInt(160, 190),
                            "hour_2", getRandomInt(0, 14),
                            "hour_3", getRandomInt(80, 90),
                            "hour_4", getRandomInt(0, 14),
                            "hour_5", getRandomInt(0, 10)
                    ),
                    "Premium", Map.of(
                            "hour_1", getRandomInt(100, 115),
                            "hour_2", getRandomInt(0, 9),
                            "hour_3", getRandomInt(50, 70),
                            "hour_4", getRandomInt(0, 10),
                            "hour_5", getRandomInt(0, 8)
                    )
            ));

            return sales;
        }

        private static List<Integer> retrieve(String stallName, String productNo) {
            List<Integer> list = new ArrayList<>();
            Map<String, Map<String, Map<String, Integer>>> sales = generateSales();

            for (String stall : sales.keySet()) {
                if (stall.equals(stallName)) {
                    for (String product : sales.get(stall).keySet()) {
                        if (product.equals(productNo)) {
                            for (String hour : sales.get(stall).get(product).keySet()) {
                                list.add(sales.get(stall).get(product).get(hour));
                            }
                        }
                    }
                }
            }

            return list;
        }

        private static int rate(String stallName, String productNo) {
            List<Integer> list = new ArrayList<>();

            for (int i = 0; i < 1000; i++) {
                List<Integer> sales = retrieve(stallName, productNo);
                list.add(sales.stream().mapToInt(Integer::intValue).sum());
            }

            return (int) list.stream().mapToInt(Integer::intValue).average().orElse(0);
        }

       public static Integer getResult(String stallname, String prodname){

        result = rate(stallname, prodname);
        return result;

        }


}


