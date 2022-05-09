let createError = require('http-errors');
let express = require('express');
let path = require('path');
let cookieParser = require('cookie-parser');
let logger = require('morgan');
require('dotenv').config();


let app = express();

//Modulo para generar token de autenticacion
let jwt = require('jsonwebtoken');
app.set('jwt', jwt);

// Módulo para leer cuerpo de peticiones posts
let bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

//Modulo encriptar
let crypto = require('crypto');
app.set('clave','abcdefg');
app.set('crypto', crypto);

// Cliente mongo
console.log(process.env)
const { MongoClient } = require("mongodb");
const url = "mongodb+srv://admin:sdi@cluster0.hz8vy.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
app.set('connectionStrings', url);

let expressSession = require('express-session');
app.use(expressSession({
  secret: 'abcdefg',
  resave: true,
  saveUninitialized: true
}));


// Repositorios
let usersRepository = require("./repositories/usersRepository.js");
usersRepository.init(app, MongoClient);
const friendshipsRepository = require("./repositories/friendshipsRepository.js");
friendshipsRepository.init(app, MongoClient);
let postsRepository = require("./repositories/postsRepository.js");
postsRepository.init(app, MongoClient);
let messagesRepository = require("./repositories/messagesRepository.js");
messagesRepository.init(app, MongoClient);

// Ruta index
let indexRouter = require('./routes/index');
let userSessionRouter = require('./routes/userSessionRouter');
let adminSessionRouter = require('./routes/adminSessionRouter');

app.use("/users/list", userSessionRouter);
app.use("/users/admin/list", adminSessionRouter);
app.use("/users/delete", adminSessionRouter);




// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'twig');

// Logger
app.use(logger('dev'));

// Uso de json para las respuestas
app.use(express.json());

// Codificación de urls
app.use(express.urlencoded({ extended: false }));

// Uso de cookies
app.use(cookieParser());

// Directorio público del proyecto
app.use(express.static(path.join(__dirname, 'public')));


// Rutas app
require("./routes/users.js")(app, usersRepository);
require("./routes/posts.js")(app, postsRepository, friendshipsRepository);
require("./routes/friendships.js")(app, friendshipsRepository, usersRepository)

require("./api/routes/UsersAPIv1.0.js")(app, usersRepository);
require("./api/routes/MessagesAPIv1.0.js")(app, friendshipsRepository,messagesRepository);


// Usar rutas index
app.use('/', indexRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
