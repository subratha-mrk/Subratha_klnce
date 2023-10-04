import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

// Define an interface for observers
interface TaskObserver {
    void taskCompleted(Task task);

    void taskPending(Task task);
}

// Task class
class Task {
    private String description;
    private boolean completed;
    private String dueDate;
 Task(){
        
    }
    public Task(String description, String dueDate) {
        this.description = description;
        this.dueDate = dueDate;
        this.completed = false;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markAsCompleted() {
        completed = true;
    }

      public void markAsCompleted(boolean s) {
        completed = s;
        
    }
    public void markAsPending() {
        completed = false;
    }

    // Memento Pattern: Create a memento
    public TaskMemento createMemento() {
        return new TaskMemento(this);
    }

    // Memento Pattern: Restore from a memento
    public void restoreFromMemento(TaskMemento memento) {
        this.description = memento.getState().getDescription();
        this.dueDate = memento.getState().getDueDate();
        this.completed = memento.getState().isCompleted();
    }
}

// Memento Pattern: TaskMemento class
class TaskMemento {
    private Task state;

    public TaskMemento(Task state) {
        this.state = new Task(state.getDescription(), state.getDueDate());
        this.state.markAsCompleted(state.isCompleted());
    }

    public Task getState() {
        return state;
    }
}

// TaskManager class
class TaskManager extends Task {
    private List<Task> tasks;
    private Stack<TaskMemento> history = new Stack<>();
    private Stack<TaskMemento> redoStack = new Stack<>();
    private List<TaskObserver> observers = new ArrayList<>();

   
    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TaskObserver observer) {
        observers.remove(observer);
    }

    public void addTask(String description, String dueDate) {
        tasks.add(new Task(description, dueDate));
    }

    public void markTaskAsCompleted(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsCompleted();
            notifyTaskCompleted(tasks.get(index));
            saveState();
        }
    }

    public void markTaskAsPending(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsPending();
            notifyTaskPending(tasks.get(index));
            saveState();
        }
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            saveState();
        }
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    // Memento Pattern: Save the current state
    private void saveState() {
        redoStack.clear();
    }



    private void restoreState(TaskMemento memento) {
        tasks.clear();
        // tasks.addAll(memento.getState());
        
    }

    // Observer Pattern: Notify task completion
    private void notifyTaskCompleted(Task task) {
        for (TaskObserver observer : observers) {
            observer.taskCompleted(task);
        }
    }

    // Observer Pattern: Notify task marked as pending
    private void notifyTaskPending(Task task) {
        for (TaskObserver observer : observers) {
            observer.taskPending(task);
        }
    }
}

// NotificationService class implementing TaskObserver
class NotificationService implements TaskObserver {
    @Override
    public void taskCompleted(Task task) {
        System.out.println("Task completed: " + task.getDescription());
    }

    @Override
    public void taskPending(Task task) {
        System.out.println("Task marked as pending: " + task.getDescription());
    }
}

 class App {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        NotificationService notificationService = new NotificationService();
        taskManager.addObserver(notificationService);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Options:");
            System.out.println("1. Add Task");
            System.out.println("2. Mark Task as Completed");
            System.out.println("3. Mark Task as Pending");
            System.out.println("4. Delete Task");
            System.out.println("5. List All Tasks");
        
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter due date: ");
                    String dueDate = scanner.nextLine();
                    taskManager.addTask(description, dueDate);
                    System.out.println("Task added.");
                    break;
                case 2:
                    System.out.print("Enter task index to mark as completed: ");
                    int completedIndex = scanner.nextInt();
                    taskManager.markTaskAsCompleted(completedIndex);
                    System.out.println("Task marked as completed.");
                    break;
                case 3:
                    System.out.print("Enter task index to mark as pending: ");
                    int pendingIndex = scanner.nextInt();
                    taskManager.markTaskAsPending(pendingIndex);
                    System.out.println("Task marked as pending.");
                    break;
                case 4:
                    System.out.print("Enter task index to delete: ");
                    int deleteIndex = scanner.nextInt();
                    taskManager.deleteTask(deleteIndex);
                    System.out.println("Task deleted.");
                    break;
                case 5:
                    System.out.println("All Tasks:");
                    List<Task> tasks = taskManager.getAllTasks();
                    for (int i = 0; i < tasks.size(); i++) {
                        Task task = tasks.get(i);
                        String status = task.isCompleted() ? "Completed" : "Pending";
                        System.out.println(i + ". Description: " + task.getDescription() +
                                " | Due Date: " + task.getDueDate() +
                                " | Status: " + status);
                    }
                    break;
                
                    case 6:
                    System.out.println("Exiting the application.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
                }
            }
        }
    }
