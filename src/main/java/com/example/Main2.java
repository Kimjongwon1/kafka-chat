package com.example;

public class Main2 {
    public static void main(String[] args) {
        //합이 S 이상이 되는 가장 짧은 연속 부분 수열의 길이를 구하라.
        //그런 수열이 없으면 0을 출력하라.
        int[] arr = {5, 1, 3, 5, 10, 7, 4, 9, 2, 8};
        int S = 15;
        int sum = 0;
        int min = Integer.MAX_VALUE;
        int left = 0;
        int right = 0;
        for(int i = 0; i<arr.length; i++){
            sum += arr[i];
            right = i;
            while (sum>=S){
              min =  Math.min(min,right - left + 1);
              sum -= arr[left++];

            }
        }
        System.out.println(min == Integer.MAX_VALUE ? 0 : min);
     }
}
