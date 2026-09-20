package mu.alche.library.Models;

public class Location {

        //this is the  unique Book id
        private int id;

        // the actual location name"
        private String name;

        // The type of the location
        private String type;

        //The parent id of the location
        private int parentLocationId;

        // constructor, fills both fields when a Location object is created
        public Location(int id, String name, String type, int parentLocationId) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.parentLocationId = parentLocationId;
        }


        public int getId() {return id;}
        public void setId(int id) {this.id = id;}

        public String getName() {return name;}
        public void setName(String name) {this.name = name;}

        public String getType() {return type;}
        public void setType(String type) {this.type = type;}

        public Integer getParentLocationId() {return parentLocationId;}
        public void setParentLocationId(Integer parentLocationId) {this.parentLocationId = parentLocationId;}
    }



