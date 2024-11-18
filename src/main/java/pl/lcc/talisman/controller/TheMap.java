package pl.lcc.talisman.controller;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.util.List;

public class TheMap {
    int rowLength;
    int columnLength;
    int tileWidth;
    int tileHeight;
    List<Node> tiles;

    public TheMap(int rowLength, int columnLength, int tileWidth, int tileHeight) {
        this.rowLength = rowLength;
        this.columnLength = columnLength;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public List<Node> getTiles() {
        return tiles;
    }

    public void updateTile(int pos, String... codes){
            tiles.set(pos, setTile(codes[0]));
    }

    public void updateAllTiles(List<List<String>> codes){
        tiles = codes.stream().map(list-> (Node) setTile(list.get(0))).toList();
    }

    private Label setTile(String code){
       var label =  switch (code){
           case "empty" -> new Label(".");
           case "up" -> new Label("^");
           case "down" -> new Label("v");
           case "left" -> new Label("<");
           case "right" -> new Label(">");
           case "north" -> new Label("^");
           case "south" -> new Label("v");
           case "east" -> new Label(">");
           case "west" -> new Label("<");
           case "food" -> new Label("*");
           default -> new Label("?");
       };
       label.setMinSize(tileWidth, tileHeight);
       label.setFont(new Font(30));
       label.setAlignment(Pos.CENTER);
       return label;
    }

}
