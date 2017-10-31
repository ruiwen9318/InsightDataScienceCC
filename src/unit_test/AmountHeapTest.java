import static org.junit.Assert.*;
import java.math.BigInteger;
import org.junit.Test;

public class AmountHeapTest {

	@Test
	public void testAdd() {
		AmountHeap heap = new AmountHeap();
		BigInteger result = heap.add(new BigInteger("400"));
		assertEquals(new BigInteger("400"), result);
		result = heap.add(new BigInteger("500"));
		assertEquals(new BigInteger("450"), result);
		result = heap.add(new BigInteger("410"));
		assertEquals(new BigInteger("410"), result);
		result = heap.add(new BigInteger("481"));
		assertEquals(new BigInteger("446"), result);
	}

}
