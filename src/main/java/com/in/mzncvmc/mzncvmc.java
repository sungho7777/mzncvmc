package com.in.mzncvmc;


import java.util.*;

public class mzncvmc {
    private static String[] uniqueArr;

    public static void main(String[] args){

        String[] source = {
                "NXPxJ-GjIlUksof24joViUpx4wXr_Xa7atKNUW-2",
                "NXPxJ-GjIlUksof24joViUpx4wXr_Xa7atKNUW-2",
                "NXPxJ-GjIlUksof24joViUpx4wXr_Xa7atKNUW-2",
                "EHdlFGCxR7TQmuL10NrLFxAAsRItBv4A8kFgZ6yI",
                "EHdlFGCxR7TQmuL10NrLFxAAsRItBv4A8kFgZ6yI",
                "rRTuMKX45CbtXDHO4fQ-Nhn01DCyibEjdyo5Cjto",
                "rRTuMKX45CbtXDHO4fQ-Nhn01DCyibEjdyo5Cjto"
        };

// 값별 현재 순번 기록용
        Map<String, Integer> sequenceMap = new HashMap<>();

// 최종 저장 데이터
        List<String[]> result = new ArrayList<>();

        for(String s : source){
            int seq = sequenceMap.getOrDefault(s, 0) + 1;
            sequenceMap.put(s, seq);

            // (값, 순번) 저장
            result.add(new String[]{s, String.valueOf(seq)});


            System.out.println(s + " , " + String.valueOf(seq));
        }



    }
}
