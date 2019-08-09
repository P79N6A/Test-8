package test;

import java.util.*;

public class Demo1 {

	public static void main(String[] args) {
		String[] resources = "20480".split(",");
		System.out.println(resources[1]);
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("一");
		arrayList.add("二");
		arrayList.add("三");
		arrayList.add("四");
		arrayList.add("五");
		remove1(arrayList,"一");
		System.out.println(arrayList);
		arrayList.remove(0);
		System.out.println(arrayList);
	}
	private static void remove1(List<String> list,String tag){
		Iterator<String> iter = list.iterator();
		while (iter.hasNext()) {
			String s=iter.next();
			if (s.equals(tag)) {
				iter.remove();
			}
		}
	}
}