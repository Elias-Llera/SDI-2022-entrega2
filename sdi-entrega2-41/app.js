let createError = require('http-errors');
let express = require('express');
let path = require('path');
let cookieParser = require('cookie-parser');
let logger = require('morgan');
require('dotenv').config();

let app = express();

// Añadimos las cabeceras mas permisivas de Access-Cotrol-Allow-Origin para todas las peticiones
app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Credentials", "true");
  res.header("Access-Control-Allow-Methods", "POST, GET, DELETE, UPDATE, PUT");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, token")
  // Debemos especificar todas las headers que se aceptan. Content-Type , token
  next();
});

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

app.use("/users/list", userSessionRouter);

const userTokenRouter = require('./api/routes/userTokenRouter');
app.use("/api/v1.0/friends/", userTokenRouter);
app.use("/api/v1.0/messages/", userTokenRouter);

// Rutas app
require("./routes/users.js")(app, usersRepository);
require("./routes/posts.js")(app, postsRepository, friendshipsRepository);
require("./routes/friendships.js")(app, friendshipsRepository, usersRepository)

require("./api/routes/UsersAPIv1.0.js")(app, usersRepository, friendshipsRepository);
require("./api/routes/MessagesAPIv1.0.js")(app, friendshipsRepository, messagesRepository);

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'twig');

// Logger
app.use(logger('dev'));

// Uso de json para las respuestas
app.use(express.json());

// Codificacion de urls
app.use(express.urlencoded({ extended: false }));

// Uso de cookies
app.use(cookieParser());

// Directorio público del proyecto
app.use(express.static(path.join(__dirname, 'public')));

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
