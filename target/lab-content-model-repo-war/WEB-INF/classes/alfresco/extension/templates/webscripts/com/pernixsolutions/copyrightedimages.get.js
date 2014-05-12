model.username = person.properties.userName;
model.userhome = userhome;

var pictures = new Array();
for (i in userhome.children) {
    if (userhome.children[i].name == "Pictures") {
        var index = 0;
        for (j in userhome.children[i].children) {
            picture = userhome.children[i].children[j];
            if (picture.hasAspect("fsc:copyrighted")){
                pictureProps = userhome.children[i].children[j].properties;
                pictures[index] = pictureProps.name;
                if (pictureProps["fsc:copyright"] && (pictureProps["fsc:copyright"].length > 0)) {
                    pictures[index] = pictureProps.name + " : " + pictureProps["fsc:copyright"];
                }
                index++;
            }
        }
    }
}
model.pictures = pictures;
