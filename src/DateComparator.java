import java.util.Comparator;

public class DateComparator implements Comparator<String> {
	@Override
	public int compare(String a, String b) {
		String aYear = a.substring(4, 8);
		String bYear = b.substring(4, 8);
		String aMonth = a.substring(0, 4);
		String bMonth = b.substring(0, 4);
		int temp = aYear.compareTo(bYear);
		if (temp != 0)
			return temp;
		else
			return aMonth.compareTo(bMonth);
	}
}