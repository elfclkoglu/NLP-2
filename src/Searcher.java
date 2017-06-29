import java.util.HashMap;
import java.util.HashSet;

public class Searcher{
	HashSet<String> uniqueWords = new HashSet<String>();
	HashSet<String> uniqueTags = new HashSet<String>();
	HashMap<Integer, WordWithTag> headsofSentence = new HashMap<Integer, WordWithTag>();
	HashMap<Integer, WordWithTag> allofWords = new HashMap<Integer, WordWithTag>();
	private HashMap<String, HashMap<String, Double>> wordWithTags = new HashMap<String, HashMap<String, Double>>();
	private HashMap<String, HashMap<String, Double>> tagWithNextTags = new HashMap<String, HashMap<String, Double>>();
	private HashMap<String, Integer> tagWithCount = new HashMap<String, Integer>();
	private Tag currentTag;

	public Searcher(HashMap<Integer, WordWithTag> allofWords, HashMap<Integer, WordWithTag> headsofSentence) {
		this.allofWords = allofWords;
		this.headsofSentence = headsofSentence;
		findUniqueTagsandWords();

	}

	public void findUniqueTagsandWords() {
		String nextTag = "";
		String secondHead = "";
		for (int i = 0; i < allofWords.size(); i++) {
			String tag = allofWords.get(i).getTag();

			String word = allofWords.get(i).getWord();
			if (i + 1 < allofWords.size()) {
				nextTag = allofWords.get(i + 1).getTag();
				secondHead = allofWords.get(i + 1).getHeadofSentence();
			}

			if (!uniqueTags.contains(tag)) {
				uniqueTags.add(tag);

			}
			if (!tagWithCount.containsKey(tag)) {
				tagWithCount.put(tag, 1);
			}
			else if (tagWithCount.containsKey(tag)) {
				tagWithCount.put(tag, tagWithCount.get(tag) + 1);
			}
			if (!uniqueWords.contains(word)) {
				uniqueWords.add(word);
				wordWithTags.put(word, new HashMap<String, Double>());

				addTag(word, tag);
			}
			/* if the word exists here, then add tags. */
			else {

				addTag(word, tag);
			}
			if ((i+1) < allofWords.size()) {
				setNextTags(tag, nextTag, secondHead);
			}
		}

	}

	public void addTag(String word, String tag) {
		HashMap<String, Double> controllerHash = new HashMap<String, Double>();
		currentTag = new Tag(tag, 0);
		if (wordWithTags.containsKey(word)) {
			controllerHash = wordWithTags.get(word);
			if (wordWithTags.get(word).containsKey(tag)) {
				double tagValue = controllerHash.get(tag);
				controllerHash.put(tag, tagValue + 1);
				wordWithTags.put(word, controllerHash);
			} else {
				controllerHash.put(tag, 1.0);
				wordWithTags.put(word, controllerHash);
			}
		}

	}

	public void setNextTags(String tag, String nextTag, String secondHead) {
		if (tagWithNextTags.containsKey(tag)) {
			addSecondTag(tag, nextTag, secondHead);
		} else if (!tagWithNextTags.containsKey(tag)) {
			tagWithNextTags.put(tag, new HashMap<String, Double>());
			addSecondTag(tag, nextTag, secondHead);
		}
	}

	public void addSecondTag(String firstTag, String secondTag, String secondHead) {
		HashMap<String, Double> controllerHash = new HashMap<String, Double>();

		if (tagWithNextTags.containsKey(firstTag) && secondHead.equals("no")) {
			controllerHash = tagWithNextTags.get(firstTag);
			if (tagWithNextTags.get(firstTag).containsKey(secondTag)) {

				double tagValue = controllerHash.get(secondTag);
				controllerHash.put(secondTag, tagValue + 1);
				tagWithNextTags.put(firstTag, controllerHash);
			} else {
				controllerHash.put(secondTag, 1.0);
				tagWithNextTags.put(firstTag, controllerHash);
			}
		}

	}

	public HashMap<String, HashMap<String, Double>> getWordWithTags() {
		return wordWithTags;
	}

	public HashMap<String, HashMap<String, Double>> getTagWithNextTags() {
		return tagWithNextTags;
	}

	public void setWordWithTags(HashMap<String, HashMap<String, Double>> wordWithTags) {
		this.wordWithTags = wordWithTags;
	}

	public HashMap<Integer, WordWithTag> getAllofWords() {
		return allofWords;
	}

	public HashMap<Integer, WordWithTag> getHeadsofSentence() {
		return headsofSentence;
	}

	public HashSet<String> getUniqueTags() {
		return uniqueTags;
	}

	public HashSet<String> getUniqueWords() {
		return uniqueWords;
	}

	public HashMap<String, Integer> getTagWithCount() {
		return tagWithCount;
	}

	public void setTagWithCount(HashMap<String, Integer> tagWithCount) {
		this.tagWithCount = tagWithCount;
	}

}
