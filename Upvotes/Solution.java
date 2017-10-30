import java.io.*;
import java.lang.*;
import java.util.*;

public class Solution {
	public static void main(String[] args) {
		try {
			upvotes();
		} catch (IOException e) {
			System.out.println(e);
		}
    }

	private static void upvotes() throws IOException {
		Scanner scanner = null;
		try {
			scanner = new Scanner(System.in);

			int nDays = scanner.nextInt();
			int windowWidth = scanner.nextInt();
			windowWidth = Math.min(windowWidth, nDays);

			if (windowWidth == 1) {
				for (int i=0; i<nDays; i++) {
					System.out.println(0);
				}
				return;
			}

			int lastVote = scanner.nextInt();
			long nIncRanges = 0;
			long nDecRanges = 0;
			// boundaries of non-decreasing subranges
			List<Integer[]> incRanges = new LinkedList<Integer[]>();
			// boundaries of non-increasing subranges
			List<Integer[]> decRanges = new LinkedList<Integer[]>();
			int lInc = 0;
			int lDec = 0;
			int r = 1;
			while (r < windowWidth) {
				int curVote = scanner.nextInt();
				if (lastVote > curVote) {
					if ( r - lInc > 1 ) {
						Integer[] range = new Integer[] { lInc, r-1 };
						incRanges.add( range );
						nIncRanges += (long) (r -lInc)*(r-1-lInc)/2;
					}
					lInc = r;
				}
				if (lastVote < curVote) {
					if ( r - lDec > 1 ) {
						Integer[] range = new Integer[] { lDec, r-1 };
						decRanges.add( range );
						nDecRanges += (long) (r -lDec)*(r-1-lDec)/2;
					}
					lDec = r;
				}
				r++;
				lastVote = curVote;
			}
			if ( r - lInc > 1 ) {
				Integer[] range = new Integer[] { lInc, r-1 };
				incRanges.add( range );
				nIncRanges += (long) (r -lInc)*(r-1-lInc)/2;
			}
			if ( r - lDec > 1 ) {
				Integer[] range = new Integer[] { lDec, r-1 };
				decRanges.add( range );
				nDecRanges += (long) (r -lDec)*(r-1-lDec)/2;
			}
				  
			// first result
			System.out.println(nIncRanges - nDecRanges);

			int curDay = windowWidth;

			while (curDay < nDays) {
				int curVote = scanner.nextInt();
				nIncRanges = updateRanges(incRanges, lastVote, curVote, curDay,
										  windowWidth, true, nIncRanges);
				nDecRanges = updateRanges(decRanges, lastVote, curVote, curDay,
										  windowWidth, false, nDecRanges);
				//System.out.println("inc: " + nIncRanges + " dec: " + nDecRanges);
				System.out.println(nIncRanges - nDecRanges);
				//System.out.println(curDay);
				
				lastVote = curVote;
				curDay++;
			}
		} finally {
			if (scanner != null) { scanner.close(); }
		}
	}


	/**
	 * Update the ranges given the new upvote number
	 * @param ranges List of existing ranges from previous window.
	 * @param lastVote Number of last up votes.
	 * @param curVote Number of current up votes.
	 * @param curDay Index of current day.
	 * @param windowWidth The width of the window.
	 * @param isInc If updating non-decreasing ranges.
	 * @param nRanges Number of ranges before update.
	 * @return Number of ranges after update.
	 */
	private static long updateRanges(List<Integer[]> ranges, int lastVote, int curVote,
									 int curDay, int windowWidth, boolean isInc,
									 long nRanges) {
		int firstDayInWindow = curDay - windowWidth + 1;
		if (ranges.size() > 0) {
			Integer[] range0 = ranges.get(0);
			if (range0[0] < firstDayInWindow) {
				nRanges -= (range0[1] - range0[0]);
				range0[0] = firstDayInWindow;
			}
			Integer[] range = ranges.get(ranges.size()-1);
			if (range[1] == curDay-1 && ((isInc && curVote-lastVote >= 0) ||
										 ((!isInc) && curVote-lastVote <= 0) ) ) {
				range[1] = curDay;
				nRanges += (long) range[1] - range[0];
			} else if (range[1] < curDay-1 &&
					   ((isInc && lastVote <= curVote) ||
						(!isInc && lastVote >= curVote))) {
				ranges.add( new Integer[] {curDay-1, curDay} );
				nRanges += 1;
			}
			if (range0[1] - range0[0] < 1) {
				ranges.remove(0);
			}
		} else {
			if ((isInc && lastVote <= curVote) || (!isInc && lastVote >= curVote)) {
				ranges.add( new Integer[] {curDay-1, curDay} );
				nRanges = 1;
			}
		}
		return nRanges;
	}
	
}
