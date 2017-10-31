import java.math.BigInteger;
import java.util.TreeMap;

public class AmountHeap {
	public BigInteger totalAmount;
	public int totalCount;

	private int leftCount;
	private int rightCount;
	private TreeMap<BigInteger, Integer> leftTree;
	private TreeMap<BigInteger, Integer> rightTree;

	public AmountHeap() {
		this.totalAmount = new BigInteger("0");
		this.leftCount = 0;
		this.rightCount = 0;
		this.totalCount = 0;
		this.leftTree = new TreeMap<>();
		this.rightTree = new TreeMap<>();
	}

	public BigInteger add(BigInteger amount) {
		totalCount += 1;
		totalAmount = totalAmount.add(amount);
		if (rightTree.isEmpty() || amount.compareTo(rightTree.firstKey()) >= 0) {
			rightCount += 1;
			rightTree.put(amount, rightTree.getOrDefault(amount, 0) + 1);
		} else {
			leftCount += 1;
			leftTree.put(amount, leftTree.getOrDefault(amount, 0) + 1);
		}
		blance();
		return getMedian();
	}

	public void blance() {
		if (rightCount > (leftCount + 1)) {
			BigInteger temp = rightTree.firstKey();
			if (rightTree.get(temp) == 1) {
				rightTree.remove(temp);
			} else {
				rightTree.put(temp, rightTree.get(temp) - 1);
			}
			leftTree.put(temp, leftTree.getOrDefault(temp, 0) + 1);
			rightCount -= 1;
			leftCount += 1;
		}
		if (leftCount > (rightCount + 1)) {
			BigInteger temp = leftTree.lastKey();
			if (leftTree.get(temp) == 1) {
				leftTree.remove(temp);
			} else {
				leftTree.put(temp, leftTree.get(temp) - 1);
			}
			rightTree.put(temp, rightTree.getOrDefault(temp, 0) + 1);
			leftCount -= 1;
			rightCount += 1;
		}
	}

	public BigInteger getMedian() {
		if (rightCount == 0)
			return leftTree.lastKey();
		if (leftCount == 0)
			return rightTree.firstKey();
		if (rightCount == leftCount) {
			BigInteger sum = leftTree.lastKey().add(rightTree.firstKey());
			BigInteger median = sum.divide(BigInteger.valueOf(2));
			if (sum.mod(BigInteger.valueOf(2)).intValue() == 1) {
				if (sum.compareTo(new BigInteger("0")) >= 0)
					median = median.add(BigInteger.valueOf(1));
				else
					median = median.add(BigInteger.valueOf(-1));
			}
			return median;
		} else if (rightCount == (leftCount + 1)) {
			return rightTree.firstKey();
		} else {
			return leftTree.lastKey();
		}
	}
}
