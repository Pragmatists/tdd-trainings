package tdd.mimuw.movielist.movies;

import java.util.ArrayList;
import java.util.List;

public class Rating {
	public List<Integer> ratings = new ArrayList<Integer>();

	boolean noRatings() {
		return ratings.isEmpty();
	}

	int sumOf(List<Integer> ratings) {
		int result = 0;
		for (Integer integer : ratings) {
			result += integer;
		}
		return result;
	}

	int averageRating() {
		return sumOf(ratings) / ratings.size();
	}

	public void add(int rating) {
		ratings.add(rating);
	}

	int average() {
		if (noRatings())
			return 0;
		return averageRating();
	}

}