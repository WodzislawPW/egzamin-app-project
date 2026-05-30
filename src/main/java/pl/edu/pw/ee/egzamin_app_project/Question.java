package pl.edu.pw.ee.egzamin_app_project;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question
{
    private String questionText;
    private String category;
    private List<Answer> answers;
    private boolean open;

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

        setOpen();
    }

    public void docxString(XWPFRun tasksRun)
    {
        StringBuilder stringBuilder;

        tasksRun.setText(questionText);
        tasksRun.addBreak();

        int i=0;
        String letters = "abcd";

        if(answers!=null)
        {
            for (Answer a : answers)
            {
                stringBuilder = new StringBuilder();

                stringBuilder.append(letters.charAt(i));
                stringBuilder.append(") ");
                stringBuilder.append(a.toString());

                tasksRun.setText(stringBuilder.toString());
                tasksRun.addBreak();

                i++;
            }
        }

        tasksRun.addBreak();
    }

    public String toString()
    {
        return category + ": " + questionText;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o==null)
            return false;

        Question temp = (Question)o;

        if(this.toString().equals(temp.toString()))
            return true;
        else
            return false;
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

    public void randomizeAnswerOrder()
    {
        Collections.shuffle(answers);
    }

    public int giveIndexOfCorrectAnswer()
    {
        int i=0;
        for(Answer a : answers)
        {
            if(a.isCorrect())
                return i;
            i++;
        }

        return 0;
    }

    public void setOpen()
    {
        open = (answers == null);
    }

    public boolean getOpen()
    {
        return open;
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
