package Models;

public class Location {

        //this is the  unique book id
        private int id;

        // the actual location name, e.g. "Shelf A2" or "Floor 1 - Reference Section"
        private String name;

        // constructor, fills both fields when a Location object is created
        public Location(int id, String name) {
            this.id = id;
            this.name = name;
        }


        public int getId() {return id;}
        public void setId(int id) {this.id = id;}

        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
    }



