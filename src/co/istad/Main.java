package co.istad;

import co.istad.model.Topic;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private  static jdbcImpl jdbc = new jdbcImpl();
    private static Scanner input= new Scanner(System.in);
    public static void main(String[] args) throws SQLException {
        int op;
        do{
            System.out.println("-------------------------");
            System.out.println("1. Insert Topic");
            System.out.println("2. Update Topic");
            System.out.println("3. Select Topic");
            System.out.println("4. Select Topic by ID");
            System.out.println("5. Select Topic by Name");
            System.out.println("6. Delete by ID");
            System.out.println("7. Exit");
            System.out.println("--------------------------");
            System.out.print("Choose option 1-7: ");
            op = input.nextInt();
            switch (op){
                case 1:
                    System.out.println("-----------INSERT TOPIC-----------");
                    Topic topic = new Topic();
                    System.out.print("Enter name: ");
                    input.nextLine();
                    topic.setName(input.nextLine());
                    System.out.print("Enter description: ");
                    topic.setDescription(input.nextLine());
                    topic.setStatus(true);
                    insertTopic(topic);
                    System.out.println("Insert Success...");
                    break;
                case 2:
                    System.out.println("-----------UPDATE TOPIC---------");
                    Topic topicToUpdate = new Topic();
                    System.out.print("Enter id to update: ");
                    topicToUpdate.setId(input.nextInt());
                    System.out.print("Enter new name: ");
                    input.nextLine();
                    topicToUpdate.setName(input.nextLine());
                    System.out.print("Enter new description: ");
                    topicToUpdate.setDescription(input.nextLine());
                    topicToUpdate.setStatus(true);
                    updateTopic(topicToUpdate);
                    System.out.println("Update success...");
                    break;
                case 3:
                    System.out.println("-----------------------t--SELECT TOPIC---------------------------");
                    selectTopic();
                    break;
                case 4:
                    System.out.println("--------------SELECT TOPIC BY ID------------");
                    Topic idToselect = new Topic();
                    System.out.print("Enter id to update: ");
                    idToselect.setId(input.nextInt());
                    selectTopicById(idToselect.getId());
                    break;
                case 5:
                    System.out.println("--------------SELECT TOPIC BY NAME-------------");
                    Topic nameToSelect = new Topic();
                    System.out.print("Enter new name: ");
                    input.nextLine();
                    nameToSelect.setName(input.nextLine());
                    selectTopicByName(nameToSelect.getName());
                    break;
                case 6:
                    System.out.println("---------------DELETE BY ID-------------");
                    Topic topicToDelete = new Topic();
                    System.out.print("Enter id to delete: ");
                    topicToDelete.setId(input.nextInt());
                    deleteTopic(topicToDelete.getId());
                    System.out.println("Delete Success...");
                    break;
                case 7:
                    System.out.println("System terminated...");
                    break;
            }
        }while(op!=6);
        selectTopic();

    }
    // todo 1. insert
    private static void insertTopic(Topic topic){
        try(Connection con = jdbc.dataSource().getConnection()){
            String insertSql = "INSERT INTO topics(name,descriptions,status) VALUES (?,?,?)";
            PreparedStatement statement =con.prepareStatement(insertSql);
            statement.setString(1, topic.getName());
            statement.setString(2, topic.getDescription());
            statement.setBoolean(3,topic.getStatus());
            int count = statement.executeUpdate();
//            System.out.println(count);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // todo 2. update
    private static void updateTopic(Topic topic){
        try(Connection con = jdbc.dataSource().getConnection()){
            String updateSql = "UPDATE topics SET name = ?, descriptions = ?, status = ? WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(updateSql);
            statement.setString(1, topic.getName());
            statement.setString(2, topic.getDescription());
            statement.setBoolean(3,topic.getStatus());
            statement.setInt(4, topic.getId());
            int count = statement.executeUpdate();
//      System.out.println(count);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    // todo 3. select topic
    private static void selectTopic(){
        try (Connection con = jdbc.dataSource().getConnection() )
        {
            System.out.println(con.getSchema());
            // 1.Create SQL Statement
            String selectSql = "SELECT * FROM  topics";
            PreparedStatement statement= con.prepareStatement(selectSql);
            // 2.Execute SQL Statment
            ResultSet resultSet = statement.executeQuery();

            // 3.Process Result with ResultSet
            List<Topic> topics  = new ArrayList<>();
            while (resultSet.next()){
                Integer id=resultSet.getInt("id");
                String name= resultSet.getString("name");
                String description = resultSet.getString("descriptions");
                Boolean status = resultSet.getBoolean("status");
                topics.add(new Topic(id,name,description,status));
            }
            for (Topic topic:topics) {
                System.out.println(topic);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    // todo 4. select topic by id
    private static void selectTopicById(int id){
        try (Connection con = jdbc.dataSource().getConnection() )
        {
            // 1.Create SQL Statement
            String selectSql = "SELECT * FROM  topics WHERE id = ?";
            PreparedStatement statement= con.prepareStatement(selectSql);
            statement.setInt(1, id);

            // 2.Execute SQL Statement
            ResultSet resultSet = statement.executeQuery();

            // 3.Process Result with ResultSet
            while (resultSet.next()){
                String name = resultSet.getString("name");
                String description = resultSet.getString("descriptions");
                Boolean status = resultSet.getBoolean("status");
                System.out.println("Topic with id " + id + ": name=" + name + ", description=" + description + ", status=" + status);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    // todo 5. select topic by name
    private static void selectTopicByName(String name){
        try (Connection con = jdbc.dataSource().getConnection() )
        {
            // 1.Create SQL Statement
            String selectSql = "SELECT * FROM  topics WHERE name = ?";
            PreparedStatement statement= con.prepareStatement(selectSql);
            statement.setString(1, name);

            // 2.Execute SQL Statement
            ResultSet resultSet = statement.executeQuery();

            // 3.Process Result with ResultSet
            while (resultSet.next()){
                Integer id = resultSet.getInt("id");
                String description = resultSet.getString("descriptions");
                Boolean status = resultSet.getBoolean("status");
                System.out.println("Topic with name " + name + ": id=" + id + ", description=" + description + ", status=" + status);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // todo 6. delete by id
    private static void deleteTopic(int id){
        try(Connection con = jdbc.dataSource().getConnection()){
            String deleteSql = "DELETE FROM topics WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(deleteSql);
            statement.setInt(1, id);
            int count = statement.executeUpdate();
//      System.out.println(count);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
