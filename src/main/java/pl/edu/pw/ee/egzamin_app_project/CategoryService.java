package pl.edu.pw.ee.egzamin_app_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoryService
{
    private static final String FILE_PATH = "src/main/resources/pl/edu/pw/ee/egzamin_app_project/categories.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    File file;
    List<String> categories;

    public CategoryService()
    {
        file = new File(FILE_PATH);

        try
        {
            if (file.exists())
            {
                categories = mapper.readValue(file, new TypeReference<List<String>>() {});
            }
            else
            {
                categories = new ArrayList<>();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            categories = new ArrayList<>();
        }
    }

    public List<String> getCategories()
    {
        return categories;
    }

    public void saveCategory(String newCategory)
    {
        try
        {
            categories.add(newCategory);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, categories);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void removeCategory(String removedCategory)
    {
        try
        {
            categories.remove(removedCategory);

            mapper.writerWithDefaultPrettyPrinter().writeValue(file, categories);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
