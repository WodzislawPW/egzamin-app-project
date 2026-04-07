package pl.edu.pw.ee.egzamin_app_project;
import java.util.ArrayList;
import java.util.List;

public class Question
{
    private String questionText;
    private String category;
    private List<Answer> answers;

    public Question(){}

    public Question(int type, List<String> components)
    {
        category = components.removeFirst();
        questionText = components.removeFirst();

        if(type>0)
        {
            answers = new ArrayList<Answer>();

            answers.add(new Answer(components.removeFirst(), true));

            while(!components.isEmpty())
                answers.add(new Answer(components.removeFirst(), false));
        }
    }

    public void printString()
    {
        System.out.println(category);
        System.out.println(questionText);

        if(answers != null)
        {
            System.out.println(answers);
        }
        else
        {
            System.out.println("kek");
        }
    }

    public String toString()
    {
        return category + ": " + questionText;
    }

    public String answers()
    {
        if(answers==null)
            return null;

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Poprawna odpowiedź: ");
        stringBuilder.append(answers.getFirst().toString());
        stringBuilder.append("\n\nNiepoprawne odpowiedzi:\n");

        for(int i=1; i<answers.size(); i++)
        {
            stringBuilder.append("- ");
            stringBuilder.append(answers.get(i).toString());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public String getQuestionText()
    {
        return questionText;
    }

    public String getCategory()
    {
        return category;
    }

    public List<Answer> getAnswers()
    {
        return answers;
    }
}
