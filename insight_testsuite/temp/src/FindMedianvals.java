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

public class FindMedianvals {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FindMedianvals application = new FindMedianvals();
		File f = new File("../input/itcont.txt");
		Scanner scanner = new Scanner(f);
		List<String> inputList = new ArrayList<String>();
		while (scanner.hasNextLine()) {
			String eachLine = scanner.nextLine();
			inputList.add(eachLine);
		}

		application.GroupByZipAndDate(inputList);
	}

	public void GroupByZipAndDate(List<String> inputList) throws IOException {
		FileWriter fw_zip = new FileWriter("../output/medianvals_by_zip.txt");
		int len = inputList.size();
		Map<String, Map<String, AmountHeap>> IDZip = new HashMap<>();
		Map<String, Map<String, List<BigInteger>>> IDDate = new HashMap<>();
		for (int i = 0; i < len; i++) {
			String s = inputList.get(i);
			String[] infoList = s.split("\\|");

			// validation 5
			String cmteID = infoList[0];
			if (cmteID.trim().length() == 0)
				continue;

			String zipCode = infoList[10];
			String date = infoList[13];
			String amount = infoList[14];
			// validation 5
			if (amount.trim().length() == 0)
				continue;
			try {
				BigInteger amt = new BigInteger(amount);
			} catch (NumberFormatException e) {
				continue;
			}
			// validation 1 otherID
			String OtherID = infoList[15];
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
					Map<String, AmountHeap> zipMap = IDZip.get(cmteID);
					AmountHeap heap;
					BigInteger runningMedian;
					if (zipMap.containsKey(zipCode)) {
						heap = zipMap.get(zipCode);
						runningMedian = heap.add(new BigInteger(amount));
					} else {
						heap = new AmountHeap();
						runningMedian = heap.add(new BigInteger(amount));
						zipMap.put(zipCode, heap);
					}
					fw_zip.write(cmteID + "|" + zipCode + "|" + runningMedian + "|" + heap.totalCount + "|"
							+ heap.totalAmount + "\n");

				} else {
					AmountHeap heap = new AmountHeap();
					BigInteger runningMedian = heap.add(new BigInteger(amount));
					Map<String, AmountHeap> zipMap = new HashMap<>();
					zipMap.put(zipCode, heap);
					IDZip.put(cmteID, zipMap);
					fw_zip.write(cmteID + "|" + zipCode + "|" + runningMedian + "|" + heap.totalCount + "|"
							+ heap.totalAmount + "\n");
				}
			}

			// validation date
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

			// generate date file
			if (isValidDate) {
				if (IDDate.containsKey(cmteID)) {
					Map<String, List<BigInteger>> dateMap = IDDate.get(cmteID);
					if (dateMap.containsKey(date)) {
						dateMap.get(date).add(new BigInteger(amount));
					} else {
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
		fw_zip.close();
		FileWriter fw_date = new FileWriter("../output/medianvals_by_date.txt");
		Collection<String> keySet = IDDate.keySet();
		List<String> IDList = new ArrayList<String>(keySet);
		Collections.sort(IDList);
		for (int i = 0; i < IDList.size(); i++) {
			String IDKey = IDList.get(i);
			Map<String, List<BigInteger>> dateMap = IDDate.get(IDKey);
			Collection<String> dateKeySet = dateMap.keySet();
			List<String> dateList = new ArrayList<String>(dateKeySet);
			Collections.sort(dateList, new DateComparator());
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
					if (s.mod(BigInteger.valueOf(2)).intValue() == 1) {
						if (s.compareTo(new BigInteger("0")) >= 0)
							median = median.add(BigInteger.valueOf(1));
						else
							median = median.add(BigInteger.valueOf(-1));
					}
				} else {
					median = amountList.get(index);
				}
				for (BigInteger eachAmount : amountList)
					sum = sum.add(eachAmount);
				fw_date.write(IDKey + "|" + dateKey + "|" + median + "|" + amountList.size() + "|" + sum + "\n");
			}
		}
		fw_date.close();
		System.out.println("Success! The medianvals_by_zip file is in the output directory!");
		System.out.println("Success! The medianvals_by_date file is in the output directory!");
	}
}
