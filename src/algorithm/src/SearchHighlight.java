import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchHighlight {
    private WordHit[] wordHits;

    public static class WordHit {
        private char[] words;
        private String[] wordPinyin;
        private char[] wordShortPinyin;

        public WordHit(String word) {
            if (word != null && word.length() > 0) {
                words = new char[word.length()];
                wordPinyin = new String[word.length()];
                wordShortPinyin = new char[word.length()];
                for (int i = 0; i < word.length(); i++) {
                    words[i] = word.charAt(i);
                    wordPinyin[i] = PinyinHelper.convertToPinyinString(String.valueOf(words[i]), ",", PinyinFormat.WITHOUT_TONE);
                    wordShortPinyin[i] = wordPinyin[i].charAt(0);
                }
            }
        }

        //优先匹配短拼
        public String getMatched(String word) {//FIXME 如果是中文怎么处理？
            StringBuilder matched = new StringBuilder();
            //首字母查找
            if (word.length() <= wordShortPinyin.length) {
                int wIndex = 0, spyIndex = 0;
                boolean matching = false;
                while (wIndex < word.length() && spyIndex < wordShortPinyin.length) {
                    if (word.charAt(wIndex) == wordShortPinyin[spyIndex]) {//开始匹配
                        matching = true;
                        matched.append(words[spyIndex]);
                        wIndex++;
                    } else if (matching) {//当前匹配失败，继续匹配
                        matched.setLength(0);
                        wIndex = 0;
                        matching = false;
                    }
                    spyIndex++;
                }
                if (matched.length() > 0) {
                    if (wIndex >= word.length()) {//已经匹配完
                        return matched.toString();
                    }
                    matched.setLength(0);
                }
            }
            //全拼查找
            boolean matching = false;
            int wordIndex = 0;
            for (int i = 0; i < wordPinyin.length; i++) {
                int pyIndex = 0;
                while (pyIndex < wordPinyin[i].length() && wordIndex < word.length()) {
                    if (word.charAt(wordIndex) != wordPinyin[i].charAt(pyIndex)) {//不相等，继续查找下一个字
                        wordIndex = 0;
                        matching = false;
                        matched.setLength(0);
                        break;
                    }

                    if (!matching) {
                        matching = true;
                    }
                    wordIndex++;
                    pyIndex++;
                }

                if (matching && ((pyIndex >= wordPinyin[i].length() && matched.length() == 0/*忽略ron*/) || matched.length()/*rongb*/ > 0)) {
                    matched.append(words[i]);
                }
                if (wordIndex > word.length() - 1) {
                    break;
                }
            }
            if (wordIndex < word.length()) {/*rongbaoa*/
                return null;
            }
            return matched.toString();
        }
    }

    public SearchHighlight(List<String> source) {
        if (source != null && !source.isEmpty()) {
            wordHits = new WordHit[source.size()];
            for (int i = 0; i < source.size(); i++) {
                wordHits[i] = new WordHit(source.get(i));
            }
        }
    }

    public List<String> search(String search) {
        if (wordHits == null || wordHits.length == 0 || search == null || search.isEmpty()) {
            return null;
        }

        List<String> result = new ArrayList<>();
        for (WordHit hit : wordHits) {
            String matchedWord = hit.getMatched(search);
            if (matchedWord != null) {
                result.add(matchedWord);
            }
        }

        return result;
    }
}
