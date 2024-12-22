package todolistapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Task class representing a TODO task
class Task {
    private String title;
    private String description;
    private boolean isCompleted;
    private int priority;

    public Task(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return (isCompleted ? "\u2713 " : "") + title + " (Priority: " + priority + ")";
    }
}

// Main Application class
public class ToDoListApp {
    private JFrame frame;
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextArea taskDescriptionArea;

    public ToDoListApp() {
        // Initialize the main frame
        frame = new JFrame("TODO List Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Task List Model and JList
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        taskList.setFont(new Font("Arial", Font.PLAIN, 14));

        // Scroll Pane for Task List
        JScrollPane scrollPane = new JScrollPane(taskList);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Description Area
        taskDescriptionArea = new JTextArea();
        taskDescriptionArea.setEditable(false);
        taskDescriptionArea.setLineWrap(true);
        taskDescriptionArea.setWrapStyleWord(true);
        taskDescriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        frame.add(new JScrollPane(taskDescriptionArea), BorderLayout.EAST);

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Task");
        JButton editButton = new JButton("Edit Task");
        JButton deleteButton = new JButton("Delete Task");
        JButton completeButton = new JButton("Mark Complete");

        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(completeButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

        // Add Task Action
        addButton.addActionListener(e -> addTask());

        // Edit Task Action
        editButton.addActionListener(e -> editTask());

        // Delete Task Action
        deleteButton.addActionListener(e -> deleteTask());

        // Mark Task as Complete
        completeButton.addActionListener(e -> markTaskComplete());

        // Task Selection Listener
        taskList.addListSelectionListener(e -> displayTaskDescription());

        // Show the Frame
        frame.setVisible(true);
    }

    private void addTask() {
        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();
        SpinnerModel priorityModel = new SpinnerNumberModel(1, 1, 5, 1);
        JSpinner prioritySpinner = new JSpinner(priorityModel);

        Object[] message = {
            "Title:", titleField,
            "Description:", descriptionField,
            "Priority (1-5):", prioritySpinner
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add Task", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String description = descriptionField.getText();
            int priority = (int) prioritySpinner.getValue();

            if (!title.isEmpty()) {
                taskListModel.addElement(new Task(title, description, priority));
            } else {
                JOptionPane.showMessageDialog(frame, "Title cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editTask() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            JTextField titleField = new JTextField(selectedTask.getTitle());
            JTextField descriptionField = new JTextField(selectedTask.getDescription());
            SpinnerModel priorityModel = new SpinnerNumberModel(selectedTask.getPriority(), 1, 5, 1);
            JSpinner prioritySpinner = new JSpinner(priorityModel);

            Object[] message = {
                "Title:", titleField,
                "Description:", descriptionField,
                "Priority (1-5):", prioritySpinner
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Edit Task", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                selectedTask.setTitle(titleField.getText());
                selectedTask.setDescription(descriptionField.getText());
                selectedTask.setPriority((int) prioritySpinner.getValue());
                taskList.repaint();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No task selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTask() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            taskListModel.removeElement(selectedTask);
            taskDescriptionArea.setText("");
        } else {
            JOptionPane.showMessageDialog(frame, "No task selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void markTaskComplete() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            selectedTask.setCompleted(true);
            JOptionPane.showMessageDialog(frame, "Task marked as complete!", "Success", JOptionPane.INFORMATION_MESSAGE);
            taskList.repaint();
        } else {
            JOptionPane.showMessageDialog(frame, "No task selected!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayTaskDescription() {
        Task selectedTask = taskList.getSelectedValue();
        if (selectedTask != null) {
            taskDescriptionArea.setText("Title: " + selectedTask.getTitle() + "\n\n" +
                                       "Description: " + selectedTask.getDescription() + "\n\n" +
                                       "Priority: " + selectedTask.getPriority() + "\n\n" +
                                       "Status: " + (selectedTask.isCompleted() ? "Completed" : "Incomplete"));
        } else {
            taskDescriptionArea.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoListApp::new);
    }
}
