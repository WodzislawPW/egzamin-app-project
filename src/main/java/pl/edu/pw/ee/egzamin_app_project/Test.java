package pl.edu.pw.ee.egzamin_app_project;

import java.util.ArrayList;
import java.util.List;

public class Test
{
    private String name;
    private List<Question> questions;

    public Test(){}

    public Test(String name)
    {
        this.name = name;
        questions = new ArrayList<Question>();
    }

    @Override
    public String toString()
    {
        return name;
    }

    public void addQuestion(Question newQuestion)
    {
        questions.add(newQuestion);
    }

    public void removeQuestion(Question removedQuestion)
    {
        questions.remove(removedQuestion);
    }

    public String getName()
    {
        return name;
    }

    public List<Question> getQuestions()
    {
        return questions;
    }
}
