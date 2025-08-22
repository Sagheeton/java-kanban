package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

class InMemoryHistoryManager implements HistoryManager {
    List<Task> history = new ArrayList<>(MAX_HISTORY_SIZE);

    @Override
    public void add(Task task) {
        if (history.size() == MAX_HISTORY_SIZE) {
            history.removeFirst();
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
