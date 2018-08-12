package thh;


public class TimeTester{
	public static void main(String[] args) {
		new TimeTester();
	}
	public TimeTester() {
		long timeTemp = System.currentTimeMillis();
		System.out.println(System.currentTimeMillis() - timeTemp);
		System.out.println(System.currentTimeMillis() - timeTemp);
	}
}
