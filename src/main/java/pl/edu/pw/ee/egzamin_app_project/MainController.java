package pl.edu.pw.ee.egzamin_app_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable
{
    @FXML
    private Label correctAnswerLabel, incorrectAnswerLabel1, incorrectAnswerLabel2, incorrectAnswerLabel3, missingElementsLabel, categoryStatusLabel;

    @FXML
    private TextArea questionTextArea, answersTextArea;

    @FXML
    private TextField correctAnswerTextField, incorrectAnswerTextField1, incorrectAnswerTextField2,incorrectAnswerTextField3, categoryTextField, searchTextField;

    @FXML
    private Button confirmButton, clearButton, addCategoryButton, deleteCategoryButton, deleteQuestionButton;

    @FXML
    private ChoiceBox<String> questionAmountChoiceBox, categoryChoiceBox;

    @FXML
    private ListView<Question> questionListView;

    private String[] incorrectAnswerAmount = {"Otwarte", "1", "2", "3"};
    private String questionType = incorrectAnswerAmount[3];
    private int questionAmount = 3;
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), e -> fadeOutMissingLabel()));

    QuestionService questionService;
    CategoryService categoryService;

    private List<String> categories;
    private boolean categoryExists = true;
    private boolean labelEmpty = true;

    Question forDeletion;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1)
    {
        questionAmountChoiceBox.getItems().addAll(incorrectAnswerAmount);
        questionAmountChoiceBox.getSelectionModel().select(3);
        questionAmountChoiceBox.setOnAction(this::switchAnswerAmount);

        categoryService = new CategoryService();

        inputCategories();

        questionService = new QuestionService();
        setupListView();

        updateCategoryButtons();

        answersTextArea.setEditable(false);
        answersTextArea.setVisible(false);

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();

        deleteQuestionButton.setDisable(true);

        //System.out.println(categoryChoiceBox.getSelectionModel().getSelectedItem());
    }

    public void deleteQuestion(ActionEvent event)
    {
        questionService.deleteCategory(forDeletion);
        deleteQuestionButton.setDisable(true);
        updateListView();
    }

    public void updateKeyword(KeyEvent event)
    {
        String text = searchTextField.getText();
        questionService.setKeyword(text);
        updateListView();
    }

    public void updateCategoryStatusLabelBuffer(KeyEvent event)
    {
        updateCategoryStatusLabel();
    }

    public void updateCategoryStatusLabel()
    {
        String text = categoryTextField.getText();

        if(categories.contains(text))
        {
            categoryStatusLabel.setText("Taka kategoria już istnieje");
            categoryExists = true;
        }
        else
        {
            categoryStatusLabel.setText("Nowa kategoria");
            categoryExists = false;
        }

        if(text.isEmpty())
        {
            categoryStatusLabel.setText("");
            categoryExists = true;
            labelEmpty = true;
        }
        else
            labelEmpty = false;

        updateCategoryButtons();
    }

    private void updateCategoryButtons()
    {
        if(categoryExists)
        {
            addCategoryButton.setDisable(true);
            deleteCategoryButton.setDisable(false);
        }
        else
        {
            addCategoryButton.setDisable(false);
            deleteCategoryButton.setDisable(true);
        }

        if(labelEmpty)
        {
            addCategoryButton.setDisable(true);
            deleteCategoryButton.setDisable(true);
        }
    }

    private void inputCategories()
    {
        categories = categoryService.getCategories();
        categoryChoiceBox.getItems().clear();
        categoryChoiceBox.getItems().addAll(categories);
    }

    public void addNewCategory(ActionEvent event)
    {
        String newCategory = categoryTextField.getText();

        categoryService.saveCategory(newCategory);
        inputCategories();

        updateCategoryStatusLabel();
    }

    public void removeCategory(ActionEvent event)
    {
        String removedCategory = categoryTextField.getText();

        categoryService.removeCategory(removedCategory);
        inputCategories();

        updateCategoryStatusLabel();
    }

    private void setupListView()
    {
        questionListView.setCellFactory(param -> new ListCell<>()
        {
            @Override
            protected void updateItem(Question q, boolean empty)
            {
                super.updateItem(q, empty);

                if (empty || q == null)
                {
                    setText(null);
                } else
                {
                    setText(q.toString());
                }
            }
        });

        updateListView();

        questionListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    forDeletion = newVal;
                    deleteQuestionButton.setDisable(false);

                    if (newVal != null && newVal.answers()!=null)
                    {
                        answersTextArea.setVisible(true);
                        answersTextArea.setText(newVal.answers());
                        //System.out.println("Wybrane: " + newVal.answers());
                    }
                    else
                        answersTextArea.setVisible(false);
                }
        );
    }

    private void updateListView()
    {
        questionListView.setItems(FXCollections.observableArrayList(questionService.getQuestions()));
    }


    public void confirmQuestion(ActionEvent event)
    {
        List<String> missingComponents = new ArrayList<>();
        List<String> presentComponents = new ArrayList<>();

        if(categoryChoiceBox.getSelectionModel().getSelectedItem()==null)
            missingComponents.add("Kategoria");
        else
            presentComponents.add(categoryChoiceBox.getSelectionModel().getSelectedItem());


        if(questionTextArea.getText().isEmpty())
            missingComponents.add("Treść pytania");
        else
            presentComponents.add(questionTextArea.getText());


        if(questionAmount!=0)
        {
            if(correctAnswerTextField.getText().isEmpty())
                missingComponents.add("Poprawna odpowiedź");
            else
                presentComponents.add(correctAnswerTextField.getText());

            if(incorrectAnswerTextField1.getText().isEmpty())
                missingComponents.add("Niepoprawna odpowiedź 1");
            else
                presentComponents.add(incorrectAnswerTextField1.getText());

            if(questionAmount>1)
            {
                if(incorrectAnswerTextField2.getText().isEmpty())
                    missingComponents.add("Niepoprawna odpowiedź 2");
                else
                    presentComponents.add(incorrectAnswerTextField2.getText());

                if(questionAmount>2)
                {
                    if(incorrectAnswerTextField3.getText().isEmpty())
                        missingComponents.add("Niepoprawna odpowiedź 3");
                    else
                        presentComponents.add(incorrectAnswerTextField3.getText());
                }
            }
        }

        if(missingComponents.isEmpty())
        {
            Question question = new Question(questionAmount, presentComponents);
            question.printString();
            questionService.saveQuestion(question);
            questionListView.setItems(FXCollections.observableArrayList(questionService.getQuestions()));
        }
        else
        {
            int n = missingComponents.size();
            StringBuilder out = new StringBuilder("Brakuje: ");

            for(int i=0; i<n; i++)
            {
                out.append(missingComponents.get(i));
                if(i!=n-1)
                    out.append(", ");
            }

            missingElementsLabel.setText(out.toString());
            missingElementsLabel.setOpacity(1);
            timeline.play();
        }
    }

    private void fadeOutMissingLabel()
    {
        missingElementsLabel.setOpacity(missingElementsLabel.getOpacity()-0.002);

        if(missingElementsLabel.getOpacity()<0)
            timeline.pause();
    }

    public void clearFields(ActionEvent event)
    {
        questionTextArea.clear();
        correctAnswerTextField.clear();
        incorrectAnswerTextField1.clear();
        incorrectAnswerTextField2.clear();
        incorrectAnswerTextField3.clear();
        categoryChoiceBox.getSelectionModel().clearSelection();
    }

    public void switchAnswerAmount(ActionEvent event)
    {
        int amount = questionAmountChoiceBox.getSelectionModel().getSelectedIndex();
        questionType = incorrectAnswerAmount[amount];
        questionAmount = amount;

        if(amount==0)
        {
            correctAnswerLabel.setVisible(false);
            correctAnswerTextField.setVisible(false);
            correctAnswerTextField.clear();

            incorrectAnswerLabel1.setVisible(false);
            incorrectAnswerTextField1.setVisible(false);
            incorrectAnswerTextField1.clear();
            incorrectAnswerLabel2.setVisible(false);
            incorrectAnswerTextField2.setVisible(false);
            incorrectAnswerTextField2.clear();
            incorrectAnswerLabel3.setVisible(false);
            incorrectAnswerTextField3.setVisible(false);
            incorrectAnswerTextField3.clear();
        }
        else
        {
            if(amount<=3)
            {
                correctAnswerLabel.setVisible(true);
                correctAnswerTextField.setVisible(true);

                incorrectAnswerLabel1.setVisible(true);
                incorrectAnswerTextField1.setVisible(true);

                incorrectAnswerLabel2.setVisible(true);
                incorrectAnswerTextField2.setVisible(true);

                incorrectAnswerLabel3.setVisible(true);
                incorrectAnswerTextField3.setVisible(true);
            }
            if(amount<=2)
            {
                incorrectAnswerLabel3.setVisible(false);
                incorrectAnswerTextField3.setVisible(false);
                incorrectAnswerTextField3.clear();
            }
            if(amount<=1)
            {
                incorrectAnswerLabel2.setVisible(false);
                incorrectAnswerTextField2.setVisible(false);
                incorrectAnswerTextField2.clear();
            }
        }
    }
}
