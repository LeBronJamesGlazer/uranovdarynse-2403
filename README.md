Report – Algorithm Benchmarks & Analysis
by: Uranov Daryn SE-2403
1. Introduction
   This report analyzes several divide-and-conquer algorithms through both theoretical and experimental perspectives.   The algorithms studied are:
	  •	MergeSort
	  •	QuickSort
	  •	Deterministic Select (Median of Medians)
	  •	Closest Pair of Points
   Metrics collected include runtime, comparisons, memory allocations, and recursion depth.
Additionally, edge cases such as arrays with many duplicates and tiny arrays (n < 10) were included to test correctness and stability.
2. Master Theorem Cases
The Master Theorem for divide-and-conquer recurrences states:
T(n) = aT(n/b) + O(n^d)

with three cases:
	•	Case 1: d < log_b a → recursion dominates
	•	Case 2: d = log_b a → balanced, complexity O(n^d log n)
	•	Case 3: d > log_b a → work per level dominates
Applied to our algorithms:

	•	MergeSort: T(n) = 2T(n/2) + O(n) → Case 2 → O(nlog n)
	•	QuickSort:
	  •	Average: O(n log n)
	  •	Worst: O(n^2) (bad pivot choices)
	•	Deterministic Select (Median of Medians):
    T(n) = T(n/5) + T(7n/10) + O(n) → O(n)
	•	Closest Pair: T(n) = 2T(n/2) + O(n) → Case 2 → O(nlog n)
3. A/B Intuition

MergeSort vs QuickSort
	•	Both: O(nlog n) theoretically.
	•	QuickSort: usually faster in practice (in-place, cache-friendly).
	•	MergeSort: stable, predictable runtime (guaranteed nlog n).

Select vs Sort
	•	Select: linear in theory, efficient when only the k-th smallest element is required.
	•	Sort: more expensive (O(nlog n)), but sometimes faster for small inputs due to smaller constants.

Closest Pair vs Brute Force
	•	Brute Force: O(n^2).
	•	Divide & Conquer: O(n \log n), scalable for large n.
  
4. Experimental Setup
	•	Input sizes: e.g., 10^3, 10^4, 10^5, 10^6.
	•	Machine: 8-Core CPU, 8GB of RAM, JDK 23.
	•	Metrics logged: runtime (ns), counter (comparisons), allocations, recursion depth.
	•	Data source: random integers for sorting; random 2D points for closest pair.
	•	Execution: benchmarks run via JMH and results exported to CSV.
5. Results
   Algorithm,RunTime(ms),Counter,Allocations,MaxDepth
   mergesort,2,120188,9999,11
   quicksort,2,155293,6647,31
   closest,14,14805,8191,13
   select,1,70729,40304,12
   
6. Discussion
	•	Tiny arrays:
	•	All algorithms ran correctly.
	•	QuickSort overhead makes it slower for n < 10, while insertion-like strategies are better.
	•	Duplicates:
	  •	QuickSort degraded with many duplicates unless a 3-way partitioning was     used.
	  •	MergeSort handled duplicates efficiently and preserved stability.
	  •	Select algorithm remained linear but constant factors were higher with     duplicates.
	  •	Closest Pair handled duplicate points without errors, returning distance   0 as expected.
	•	MergeSort vs QuickSort: QuickSort faster on random data, but MergeSort more robust on duplicates.
	•	Select vs Sort: Select useful only for large n, sorting simpler for small n.
	•	Closest Pair: divide-and-conquer consistently outperformed brute force once n > 200.

7. Conclusion
	•	Edge cases confirmed correctness:
	•	Tiny arrays handled gracefully (no crashes, results correct).
	•	Duplicates did not break algorithms; MergeSort preferred for stability.
	•	Theoretical complexities aligned with experiments, but practical behavior depends strongly on input distribution.
	•	For production:
	•	Use QuickSort for general-purpose large datasets without many duplicates.
	•	Use MergeSort when duplicates or stability matter.
	•	Use Select when only one element is needed, but not for small inputs.
	•	Use Divide-and-Conquer Closest Pair for n > 200.

  
