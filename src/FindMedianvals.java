import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class FindMedianvals {
	static class Node {
		public BigInteger totalAmount;
		public int totalCount;

		private int leftCount;
		private int rightCount;
		private TreeMap<BigInteger, Integer> leftTree;
		private TreeMap<BigInteger, Integer> rightTree;

		public Node() {
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
			if (rightTree.isEmpty() || amount.compareTo(rightTree.firstKey()) >=0) {
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
				if(sum.mod(BigInteger.valueOf(2)).compareTo(median) >= 0) {
					median = median.add(BigInteger.valueOf(1));
				}
				return median;
				
			} else if (rightCount == (leftCount + 1)) {
				return rightTree.firstKey();
			} else {
				return leftTree.lastKey();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File f = new File("../input/itcont.txt");
		Scanner scanner = new Scanner(f);
		List<String> inputList = new ArrayList<String>();
		while (scanner.hasNextLine()) {
			String eachLine = scanner.nextLine();
			inputList.add(eachLine);
		}
		FileWriter fw_zip = new FileWriter("../output/medianvals_by_zip.txt");
		int len = inputList.size();
		Map<String, Map<String, Node>> IDZip = new HashMap<>();
		Map<String, Map<String, List<BigInteger>>> IDDate = new HashMap<>();
		for (int i = 0; i < len; i++) {
			String s = inputList.get(i);
			String[] infoList = s.split("\\|");
			String cmteID = infoList[0];
			
			// validation 5
			if (cmteID.trim().length() == 0)
				continue;
			String zipCode = infoList[10];
			String date = infoList[13];
			String amount = infoList[14];
			
			// validation 5
			if (amount.trim().length() == 0)
				continue;
			String OtherID = infoList[15];
			
			// validation 1 otherID
			if (OtherID.trim().length() != 0)
				continue;
			
			// validation 3 zipCode
			boolean isValidZip = true;
			if (zipCode.length() < 5)
				isValidZip = false;
			else
				zipCode = zipCode.substring(0, 5);
			
			// generate zip file
			if (isValidZip) {
				if (IDZip.containsKey(cmteID)) {
					Map<String, Node> zipMap = IDZip.get(cmteID);
					Node temp = zipMap.getOrDefault(zipCode, new Node());
					BigInteger runningMedian = temp.add(new BigInteger(amount));
					fw_zip.write(cmteID + "|" + zipCode + "|" + runningMedian + "|" + temp.totalCount + "|"
							+ temp.totalAmount + "\n");
				} else {
					Node temp = new Node();
					BigInteger runningMedian = temp.add(new BigInteger(amount));
					Map<String, Node> zipMap = new HashMap<>();
					zipMap.put(zipCode, temp);
					IDZip.put(cmteID, zipMap);
					fw_zip.write(cmteID + "|" + zipCode + "|" + runningMedian + "|" + temp.totalCount + "|"
							+ temp.totalAmount + "\n");
				}

			}

			// valid 2 date
			boolean isValidDate = true;
			if (date.trim().length() != 8)
				isValidDate = false;
			else {
				try {
					LocalDate.parse(date,
							DateTimeFormatter.ofPattern("MMdduuuu").withResolverStyle(ResolverStyle.STRICT));
				} catch (DateTimeParseException e) {
					isValidDate = false;
				}
			}

			// generate data file
			if (isValidDate) {
				if (IDDate.containsKey(cmteID)) {
					Map<String, List<BigInteger>> dateMap = IDDate.get(cmteID);
					if(dateMap.containsKey(date)) {
						dateMap.get(date).add(new BigInteger(amount));
					}else {
						List<BigInteger> list = new ArrayList<>();
						list.add(new BigInteger(amount));
						dateMap.put(date, list);
					}
				} else {
					Map<String, List<BigInteger>> dateMap = new HashMap<>();
					List<BigInteger> list = new ArrayList<>();
					list.add(new BigInteger(amount));
					dateMap.put(date, list);
					IDDate.put(cmteID, dateMap);
				}
			}

		}

		FileWriter fw_date = new FileWriter("../output/medianvals_by_date.txt");
		
		Collection<String> keySet= IDDate.keySet();
		List<String> IDList = new ArrayList<String>(keySet);
		Collections.sort(IDList);
		for (int i = 0; i < IDList.size(); i++) {
			String IDKey = IDList.get(i);
			Map<String, List<BigInteger>> dateMap = IDDate.get(IDKey);
			Collection<String> dateKeySet= dateMap.keySet();
			List<String> dateList = new ArrayList<String>(dateKeySet);
			Collections.sort(dateList);
			for (int j = 0; j < dateList.size(); j++) {
				String dateKey = dateList.get(j);
				List<BigInteger> amountList = dateMap.get(dateKey);
				Collections.sort(amountList);
				BigInteger median;
				BigInteger sum = new BigInteger("0");
				int index = amountList.size() / 2;
				if (amountList.size() % 2 == 0) {
					BigInteger s = amountList.get(index).add(amountList.get(index - 1));
					median = s.divide(BigInteger.valueOf(2));
					if(s.mod(BigInteger.valueOf(2)).compareTo(median) >= 0) {
						median = median.add(BigInteger.valueOf(1));
					}
					
				} else {
					median = amountList.get(index);
				}
				for (BigInteger eachAmount : amountList)
					sum = sum.add(eachAmount);
				fw_date.write(IDKey + "|" + dateKey + "|" + median + "|" + amountList.size() + "|" + sum + "\n");
			}
			
		}
		fw_zip.close();
		fw_date.close();
	}
}
