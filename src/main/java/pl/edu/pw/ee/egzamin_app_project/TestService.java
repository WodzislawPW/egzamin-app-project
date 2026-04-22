package pl.edu.pw.ee.egzamin_app_project;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TestService
{
    private static final String FILE_PATH = "src/main/resources/pl/edu/pw/ee/egzamin_app_project/tests.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    File file;
    List<Test> tests;
    String keyword = "";

    public TestService()
    {
        file = new File(FILE_PATH);

        try
        {
            if (file.exists())
            {
                tests = mapper.readValue(file, new TypeReference<List<Test>>() {});
            }
            else
            {
                tests = new ArrayList<>();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            tests = new ArrayList<>();
        }
    }

    public void setKeyword(String keyword)
    {
        this.keyword = keyword;
    }

    public List<Test> getTests()
    {
        tests.sort(Comparator.comparing( (Test t) -> !t.toString().toLowerCase().contains(keyword.toLowerCase()) ));
        return tests;
    }

    public Test getTest(String testName)
    {
        for(Test t : tests)
        {
            if(t.getName().equals(testName))
                return t;
        }

        return null;
    }

    public void saveTests(Test newTest)
    {
        try
        {
            tests.add(newTest);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, tests);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void removeTest(Test removedTest)
    {
        try
        {
            tests.remove(removedTest);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, tests);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
