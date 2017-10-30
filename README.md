# Quora Challenge

This is a repository for Quora Programming Challenge at [https://www.quora.com/challenges](https://www.quora.com/challenges).

## Upvotes

- **Problem description**:  [https://www.quora.com/about/challenges#upvotes](https://www.quora.com/about/challenges#upvotes)

- **Hardness**: `Medium`. At first glance, the problem is not very hard, but it took me several trials before passing all the test cases due to optimization chances I overlooked in the first place.

- **Solution**: The number of subranges can be calculated by their lengths. For a subrange with length `n>1`, the number of subranges is `(n-1)n/2`. So, we can just find all the continuous subranges, and keep track of their start and end points using two lists: one for non-decreasing subranges and one for non-increasing subranges. Having thought through that, the problem can then be solved in two cases:
    - Case 1: Find the number of subranges in the first window. This can be solved by moving two pointers from left to right in the first window. We need to pay attention to the last subrange, and make sure to add that to the list. We also need to pay attention to the integer overflow problem.
    - Case 2: Find the number of subranges in the remaining windows. Since we have already built up the list of subranges in the previous window, we can just update the existing subranges and the number of subranges. Notice that, we only have to modify the first subrange and/or the last subrange. When doing that, we can update the number of subranges depending on if a date has been subtracted from the first subrange or added to the last range. We need to pay attention to a special case where the current date and the day before the current date formulate a new valid subrange. If that happens, we need to add the new subrange to the list.
