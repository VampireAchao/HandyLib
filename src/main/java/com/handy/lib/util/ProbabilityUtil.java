package com.handy.lib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 概率工具类
 *
 * @author hs
 * @date 2021/3/29 18:29
 */
public class ProbabilityUtil {
    private final Random random = new Random();
    private final List<Integer> probabilityNumList = new ArrayList<>();

    private ProbabilityUtil() {
    }

    private static volatile ProbabilityUtil INSTANCE;

    public static ProbabilityUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (ProbabilityUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ProbabilityUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 不加锁进行概率抽取
     *
     * @param num    概率数字
     * @param maxNum 总概率
     * @return true 抽到
     */
    public boolean pickIndex(int num, int maxNum) {
        if (num >= maxNum) {
            num = maxNum;
        }
        int[] nums = {num, maxNum - num};
        return randomIndex(nums) == 0;
    }

    /**
     * 加锁进行概率抽取
     *
     * @param num    概率数字
     * @param maxNum 总概率
     * @return true 抽到
     */
    public synchronized boolean pickSyncIndex(int num, int maxNum) {
        if (num >= maxNum) {
            num = maxNum;
        }
        int[] nums = {num, maxNum - num};
        return randomIndex(nums) == 0;
    }

    /**
     * 进行二分查询
     *
     * @param nums 数组
     * @return 返回0为匹配到了数组第一个数
     */
    private int randomIndex(int[] nums) {
        probabilityNumList.clear();
        int tot = 0;
        for (int num : nums) {
            tot += num;
            probabilityNumList.add(tot);
        }
        int randomNum = random.nextInt(tot);
        int hi = probabilityNumList.size() - 1;
        int lo = 0;
        while (lo != hi) {
            int mid = (lo + hi) / 2;
            if (randomNum >= probabilityNumList.get(mid)) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

}