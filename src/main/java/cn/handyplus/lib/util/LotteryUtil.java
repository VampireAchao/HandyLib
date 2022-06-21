package cn.handyplus.lib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 概率工具类
 *
 * @author handy
 */
public class LotteryUtil {
    private final List<LotteryUtil.ContinuousList> lotteryList = new ArrayList<>();
    private double maxElement;

    public LotteryUtil(List<Double> list) {
        if (list != null && list.size() != 0) {
            double minElement;
            LotteryUtil.ContinuousList continuousList;
            for (Double d : list) {
                minElement = this.maxElement;
                this.maxElement += d;
                continuousList = new ContinuousList(minElement, this.maxElement);
                this.lotteryList.add(continuousList);
            }
        } else {
            throw new IllegalArgumentException("抽奖集合不能为空！");
        }
    }

    public int randomIndex() {
        int index = -1;
        Random r = new Random();
        double d = r.nextDouble() * this.maxElement;
        if (d == 0.0D) {
            d = r.nextDouble() * this.maxElement;
        }
        int size = this.lotteryList.size();
        for (int i = 0; i < size; ++i) {
            LotteryUtil.ContinuousList cl = this.lotteryList.get(i);
            if (cl.isContainKey(d)) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new IllegalArgumentException("概率集合设置不合理！");
        } else {
            return index;
        }
    }

    private static class ContinuousList {
        private final double minElement;
        private final double maxElement;

        public ContinuousList(double minElement, double maxElement) {
            if (minElement > maxElement) {
                throw new IllegalArgumentException("区间不合理，minElement不能大于maxElement！");
            } else {
                this.minElement = minElement;
                this.maxElement = maxElement;
            }
        }

        public boolean isContainKey(double element) {
            return element > this.minElement && element <= this.maxElement;
        }
    }

}
