import java.io.*;
import java.lang.*;
import java.util.*;

public class Solution {
	public static void main(String[] args) {

		// file handling
		File inputFile = new File( "input.txt" );
		File outputFile = new File( "output.txt" );
        if( !inputFile.exists() ) {
            System.out.println( "Input file doesn't exist " );
        } else {
            try {
				upvotes(inputFile, outputFile);
			} catch (IOException e) {
				System.out.println(e);
			}
        }
    }

	private static void upvotes(File inputFile, File outputFile) throws IOException {
		Scanner scanner = null;
		FileOutputStream outputStream = null;
		try {
			scanner = new Scanner(inputFile);
			outputStream = new FileOutputStream( outputFile );

			int nDays = scanner.nextInt();
			int windowWidth = scanner.nextInt();
			windowWidth = Math.min(windowWidth, nDays);

			int[] firstWindow = new int[windowWidth];
			for (int i = 0; i< windowWidth; i++) {
				firstWindow[i] = scanner.nextInt();
			}
				
			// boundaries of non-decreasing subranges
			List<Integer[]> incRanges = findRanges(firstWindow, true);
			// boundaries of non-increasing subranges
			List<Integer[]> decRanges = findRanges(firstWindow, false);

			// first result
			outputStream.write(findDiffNum(incRanges, decRanges));

			int lastVote = firstWindow[windowWidth-1];
			int curDay = windowWidth;
			while (scanner.hasNextInt()) {
				int curVote = scanner.nextInt();
				updateRanges(incRanges, lastVote, curVote, curDay, windowWidth, true);
				updateRanges(decRanges, lastVote, curVote, curDay, windowWidth, false);
				//System.out.println(findDiffNum(incRanges, decRanges));
				outputStream.write(findDiffNum(incRanges, decRanges));

				lastVote = curVote;
				curDay++;
			}
			
		} finally {
			if (scanner != null) { scanner.close(); }
			if (outputStream != null) { outputStream.close(); }
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
	 */
	private static void updateRanges(List<Integer[]> ranges, int lastVote, int curVote,
									 int curDay, int windowWidth, boolean isInc) {
		int firstDayInWindow = curDay - windowWidth + 1;
		int idx = 0;
		while (idx < ranges.size()) {
			Integer[] range = ranges.get(idx);
			if (range[0] < firstDayInWindow) { range[0] = firstDayInWindow; }
			if (range[1] == curDay-1 && ((isInc && curVote-lastVote >= 0) ||
										 (!isInc && curVote-lastVote <= 0) ) ) {
				range[1] = curDay;
			}
			if (range[1] - range[0] < 1) {
				ranges.remove(idx);
				continue;
			}
			idx++;
		}
		if (ranges.size() == 0) {
			if ((isInc && lastVote <= curVote) || (!isInc && lastVote >= curVote)) {
				ranges.add( new Integer[] {curDay-1, curDay} );
			}
		}
	}
	
	/**
	 * Find the difference between the number of non-decreasing/non-increasing ranges 
	 * @param ranges List of ranges.
	 * @return Number of non-decreasing ranges minus that of non-increasing ranges
	 */
	private static int findDiffNum(List<Integer[]> incRanges, List<Integer[]> decRanges) {
		int nIncRanges = findNumRanges(incRanges);
		int nDecRanges = findNumRanges(decRanges);
		return nIncRanges - nDecRanges;
	}
	/**
	 * Find the total number of ranges
	 * @param ranges List of ranges.
	 * @return Number of ranges.
	 */
	private static int findNumRanges(List<Integer[]> ranges) {
		int nRanges = 0;
		for (Integer[] range: ranges) {
			int nDays = range[1] - range[0] + 1;
			if (nDays <= 0) { continue; }
			nRanges += (nDays-1) * nDays / 2;
		}
		return nRanges;
	}

	/**
	 * Find all non-decreasing or non-increasing ranges
	 * @param firstWindow Array of number of upvotes in the first window.
	 * @param isInc If finding non-decreasing ranges.
	 * @return All non-decreasing or non-increasing ranges.
	 */
	private static List<Integer[]> findRanges(int[] firstWindow, boolean isInc) {
		List<Integer[]> ranges = new LinkedList<Integer[]>();
		if (firstWindow == null || firstWindow.length <= 1) { return ranges; }
		int l = 0;
		int r = 1;
		while (r < firstWindow.length) {
			while (r < firstWindow.length && (
					   (isInc && firstWindow[r-1] <= firstWindow[r] ) ||
					   ((!isInc) && firstWindow[r-1] >= firstWindow[r])) ) {
				r++;
			}
			if ( r - l > 1 ) {
				Integer[] range = new Integer[] { l, r-1 };
				ranges.add( range );
			}
			l = r;
			r = l + 1;
		}
		return ranges;
	}
		
}
