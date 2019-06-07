package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.Deque;
import java.util.LinkedList;

public class Controller {
    public AnchorPane root;
    public TableView table = new TableView();
    public TableColumn sizeCol = new TableColumn();
    public TableColumn taskCol = new TableColumn();
    public Button firstFit = new Button();
    public Button bestFit = new Button();
    public Button reset = new Button();
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
            FXCollections.observableArrayList(new Block(640,-1));

    public class Task {
        public int size;
        public int task;
        //true表示申请内存，false表示释放内存
        public boolean type;
        Task(int size_ ,int task_,boolean type_) {
            size = size_;
            task = task_;
            type = type_;
        }
    }

    Deque<Task> taskSequence = new LinkedList<>();
    private void singleFirstFit() {
        bestFit.setDisable(true);
        Task now = taskSequence.poll();
        if(now == null) return;

        //当前任务是申请内存
        if(now.type == true) {
            for(int i = 0; i< memory.size(); i++) {
                if(memory.get(i).task == -1 && memory.get(i).size > now.size) {
                    memory.get(i).size -= now.size;
                    memory.add(i,new Block(now.size,now.task));
                    //首次适应找到第一个就好。没有break的话，add后size增大了，可能继续下去。
                    break;
                }
            }
        }
        //当前任务是释放内存
        else {
            for(int i =0 ;i < memory.size() ;i++) {
                if(memory.get(i).task == now.task) {
                    memory.get(i).task = -1;
                    table.refresh();
                    //先合并后边的空闲区，再合并到前边的，避免索引混乱
                    if(i <= memory.size() -2 && memory.get(i+1).task == -1) {
                        memory.get(i).size += memory.get(i+1).size;
                        memory.remove(i+1);
                    }
                    if(i>= 1 && memory.get(i-1).task == -1) {
                        memory.get(i-1).size += now.size;
                        memory.remove(i);
                    }
                }
            }
        }
    }
    private void singleBestFit() {
        firstFit.setDisable(true);
        Task now = taskSequence.poll();

        if(now == null) return;
        //当前任务是申请内存
        if(now.type == true) {
            int minSize = 999;
            int min = -1;
            for(int i = 0; i< memory.size(); i++) {
                //在满足条件的空闲区中找最小的
                if(memory.get(i).task == -1 && memory.get(i).size > now.size &&  memory.get(i).size < minSize) {
                    minSize = memory.get(i).size;
                    min = i;
                }
            }
            //如果找到了
            if(min != -1) {
                memory.get(min).size -= now.size;
                memory.add(min,new Block(now.size,now.task));
            }
        }
        //释放内存操作一样
        else {
            for(int i =0 ;i < memory.size() ;i++) {
                if(memory.get(i).task == now.task) {
                    memory.get(i).task = -1;
                    table.refresh();
                    //先合并后边的空闲区，再合并到前边的，避免索引混乱
                    if(i <= memory.size() -2 && memory.get(i+1).task == -1) {
                        memory.get(i).size += memory.get(i+1).size;
                        memory.remove(i+1);
                    }

                    if(i>= 1 && memory.get(i-1).task == -1) {
                        memory.get(i-1).size += now.size;
                        memory.remove(i);
                    }


                }


            }
        }
    }
    public void initialize() {
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        taskCol.setCellValueFactory(new PropertyValueFactory<>("task"));
        table.setItems(memory);
        taskSequence.offer(new Task(130,1,true));
        taskSequence.offer(new Task(60,2,true));
        taskSequence.offer(new Task(100,3,true));
        taskSequence.offer(new Task(60,2,false));
        taskSequence.offer(new Task(200,4,true));
        taskSequence.offer(new Task(100,3,false));
        taskSequence.offer(new Task(130,1,false));
        taskSequence.offer(new Task(140,5,true));
        taskSequence.offer(new Task(60,6,true));
        taskSequence.offer(new Task(50,7,true));
        taskSequence.offer(new Task(60,6,false));
        firstFit.setOnAction(e->singleFirstFit());
        bestFit.setOnAction(e->singleBestFit());

        reset.setOnAction(e-> {
            memory.clear();
            firstFit.setDisable(false);
            bestFit.setDisable(false);
            memory.add(new Block(640,-1));
            if(taskSequence.size() != 11) {
                taskSequence.offer(new Task(130,1,true));
                taskSequence.offer(new Task(60,2,true));
                taskSequence.offer(new Task(100,3,true));
                taskSequence.offer(new Task(60,2,false));
                taskSequence.offer(new Task(200,4,true));
                taskSequence.offer(new Task(100,3,false));
                taskSequence.offer(new Task(130,1,false));
                taskSequence.offer(new Task(140,5,true));
                taskSequence.offer(new Task(60,6,true));
                taskSequence.offer(new Task(50,7,true));
                taskSequence.offer(new Task(60,6,false));
            }
        });

    }
}
