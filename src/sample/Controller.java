package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class Controller {
    public AnchorPane root;
    public TableView table = new TableView();
    public TableColumn sizeCol = new TableColumn();
    public TableColumn taskCol = new TableColumn();
    public class Block {
        public int size;
        public int task;
        Block(int size_,int task_) {
            size = size_;
            task = task_;
        }
        public int getSize() {
            return size;
        }

        public int getTask() {
            return task;
        }
    }
    private ObservableList<Block> memory =
            FXCollections.observableArrayList(new Block(120,1));

    public void initialize() {
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        taskCol.setCellValueFactory(new PropertyValueFactory<>("task"));
        table.setItems(memory);
    }
}
