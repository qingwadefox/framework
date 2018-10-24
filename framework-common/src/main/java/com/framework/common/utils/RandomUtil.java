package com.framework.common.utils;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

public class RandomUtil {
  public static String getUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString();
  }

  public static String getSimpleUUID() {
    UUID uuid = UUID.randomUUID();
    return uuid.toString().replaceAll("-", "");
  }

  public static String ramdomNumber(int num) {
    return RandomStringUtils.random(num, false, true);
  }

  public static int randomNumber(int min, int max) {
    return new Random().nextInt(max) % (max - min + 1) + min;
  }

  public static int[] randomSplitNumber(int number, int minCount, int maxCount) {
    int[] result = new int[randomNumber(minCount, maxCount)];
    int getNumber = 0;

    for (int i = 0; i < result.length; i++) {

      if (i < result.length - 1) {
        int avgNumber = (number - getNumber) / (result.length - i);
        result[i] = randomNumber(avgNumber - avgNumber / 2, avgNumber + avgNumber / 2);
        getNumber += result[i];
      } else {
        result[i] = number - getNumber;
      }

    }
    return result;

  }

  public static void main(String[] args) {
    System.out.println(randomNumber(0, 5));

  }
}
