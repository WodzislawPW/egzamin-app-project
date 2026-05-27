package pl.edu.pw.ee.egzamin_app_project;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocxParser
{
    Test test;
    File directory;
    int groupAmount = 1;

    public void setTest(Test test)
    {
        this.test = test;
    }

    public void setDirectory(File directory) { this.directory = directory; }

    public void setGroupAmount(int groupAmount) { this.groupAmount = groupAmount; }

    public void exportTest()
    {
        String letters = "ABCDEFGHIJ";

        for(int i=0; i<groupAmount; i++)
        {
            toDocx(letters.charAt(i));
        }
    }

    public void toDocx(char group)
    {
        List<Question> questions = new ArrayList<>(test.getQuestions());
        String letters = "ABCD";

        StringBuilder testText = new StringBuilder();
        StringBuilder answerSheet = new StringBuilder();

        Collections.shuffle(questions);

        int i = 1;
        for(Question q : questions)
        {
            //System.out.println(i);
            //System.out.println(q.getOpen());

            if(!q.getOpen())
                q.randomizeAnswerOrder();

            testText.append(q.docxString());

            if(!q.getOpen())
            {
                answerSheet.append(i);
                answerSheet.append(letters.charAt(q.giveIndexOfCorrectAnswer()));
                answerSheet.append("\n");
            }
            i++;
        }

        File file = new File(directory.toString() + "\\" + test.toString() + group);

        System.out.println(file.toString());
        System.out.println(testText.toString());
        System.out.println(answerSheet.toString());
    }
}
