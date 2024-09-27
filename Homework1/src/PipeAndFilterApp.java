import java.io.*;
import java.util.*;

public class PipeAndFilterApp {

    public static void main(String[] args) throws IOException {
        SourceFilter source = new SourceFilter("pftest.txt");
        UniqueFilter unique = new UniqueFilter();
        SortFilter sort = new SortFilter();
        OutputFilter output = new OutputFilter();

        // Pipeline between SourceFilter and UniqueFilter
        source.setNextFilter(unique);
        // Pipeline between UniqueFilter and SortFilter
        unique.setNextFilter(sort);
        // Pipeline between UniqueFilter and OutputFilter
        sort.setNextFilter(output);

        // Start the pipeline
        source.process();
    }
}

// Abstract class for Filter
abstract class Filter {
    protected Filter nextFilter;

    public void setNextFilter(Filter nextFilter) {
        this.nextFilter = nextFilter;
    }

    public abstract void process(List<String> input) throws IOException;
}

// Source Filter: Get words and make them lower-case from file
class SourceFilter extends Filter {
    private String filename;

    public SourceFilter(String filename) {
        this.filename = filename;
    }

    public void process() throws IOException {
        List<String> words = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] wordArray = line.toLowerCase().split("\\W+");  // Split by non-word characters
            words.addAll(Arrays.asList(wordArray));
        }
        reader.close();
        nextFilter.process(words);
    }

    public void process(List<String> input) throws IOException {
        throw new UnsupportedOperationException("This method is not used in Source Filter.");
    }
}

// Unique Filter: Removes duplicate words
class UniqueFilter extends Filter {


    public void process(List<String> words) throws IOException {
        Set<String> uniqueWords = new HashSet<>(words);
        List<String> filteredWords = new ArrayList<>(uniqueWords);
        nextFilter.process(filteredWords);
    }
}

// Sort Filter: Sorts words alphabetically
class SortFilter extends Filter {

    public void process(List<String> words) throws IOException {
        Collections.sort(words);
        nextFilter.process(words);
    }
}

// Output Filter: Outputs sorted list to console
class OutputFilter extends Filter {


    public void process(List<String> words) throws IOException {
        for (String word : words) {
            System.out.println(word);
        }
    }
}
