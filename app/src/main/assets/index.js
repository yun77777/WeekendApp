const express = require("express");
const app = express(); 


app.get("/", (req, res) => {
    // res.send("Hello World");
    res.sendFile(__dirname + "/index.html");

});
  
app.listen(3000, () => console.log("localhost 3000)"));