package com.icefill.game;

import java.util.Collections;
import java.util.LinkedList;


public class NonRepeatRandomizer {
		private LinkedList<LookupElt> look_up=new LinkedList<LookupElt>();
		private int upper_bound;
		private int n;
		
		public NonRepeatRandomizer(int upper_bound) {
			n=this.upper_bound=upper_bound;
			for (int i=0;i<upper_bound;i++) {
				look_up.add(new LookupElt(i));
			}
		}
		public NonRepeatRandomizer(int upper_bound1, int upper_bound2) {
			n=this.upper_bound=upper_bound1*upper_bound2;
			for (int x=0;x<upper_bound1;x++) {
				for (int y=0;y<upper_bound2;y++) {
					
					look_up.add(new LookupElt(x*10+y));
				}	
			}
		}
		public NonRepeatRandomizer(int lower_bound1, int upper_bound1, int lower_bound2, int upper_bound2) {
			n=this.upper_bound=(upper_bound1-lower_bound1)*(upper_bound2-lower_bound2);
			for (int x=lower_bound1;x<upper_bound1;x++) {
				for (int y=lower_bound2;y<upper_bound2;y++) {
					look_up.add(new LookupElt(x*10+y));
				}	
			}
		}
		
		public int nextInt() {
			if (n==1)
			{
				n=upper_bound;
				return look_up.get(0).value;
				
			}
			int rn=Randomizer.nextInt(n);
			int return_value= look_up.get(rn).value;
			Collections.swap(look_up, rn,n-1 );
			n--;
			return return_value;	
		}
		public void reset() {
			n=upper_bound;
		}
		
	public static class LookupElt{
		public int value;
		public LookupElt(int value) {
			this.value=value;
		}
		
	}

}
