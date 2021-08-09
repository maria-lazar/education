require('source-map-support/register');
module.exports =
/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "/";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 0);
/******/ })
/************************************************************************/
/******/ ({

/***/ "./src/auth/index.js":
/*!***************************!*\
  !*** ./src/auth/index.js ***!
  \***************************/
/*! exports provided: router */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _router__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./router */ "./src/auth/router.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "router", function() { return _router__WEBPACK_IMPORTED_MODULE_0__["router"]; });



/***/ }),

/***/ "./src/auth/router.js":
/*!****************************!*\
  !*** ./src/auth/router.js ***!
  \****************************/
/*! exports provided: router */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "router", function() { return router; });
/* harmony import */ var koa_router__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! koa-router */ "koa-router");
/* harmony import */ var koa_router__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(koa_router__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var _store__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./store */ "./src/auth/store.js");
/* harmony import */ var jsonwebtoken__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! jsonwebtoken */ "jsonwebtoken");
/* harmony import */ var jsonwebtoken__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(jsonwebtoken__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var _utils__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../utils */ "./src/utils/index.js");




const router = new koa_router__WEBPACK_IMPORTED_MODULE_0___default.a();

const createToken = user => {
  return jsonwebtoken__WEBPACK_IMPORTED_MODULE_2___default.a.sign({
    username: user.username,
    _id: user._id
  }, _utils__WEBPACK_IMPORTED_MODULE_3__["jwtConfig"].secret, {
    expiresIn: 60 * 60 * 60
  });
};

const createUser = async (user, response) => {
  try {
    await _store__WEBPACK_IMPORTED_MODULE_1__["default"].insert(user);
    response.body = {
      token: createToken(user)
    };
    response.status = 201;
  } catch (err) {
    response.body = {
      issue: [{
        error: err.message
      }]
    };
    response.status = 400;
  }
};

router.post('/signup', async ctx => await createUser(ctx.request.body, ctx.response));
router.post('/login', async ctx => {
  const credentials = ctx.request.body;
  const response = ctx.response;
  const user = await _store__WEBPACK_IMPORTED_MODULE_1__["default"].findOne({
    username: credentials.username
  });

  if (user && credentials.password === user.password) {
    response.body = {
      token: createToken(user)
    };
    response.status = 201;
  } else {
    response.body = {
      issue: [{
        error: 'Invalid credentials'
      }]
    };
    response.status = 400;
  }
});

/***/ }),

/***/ "./src/auth/store.js":
/*!***************************!*\
  !*** ./src/auth/store.js ***!
  \***************************/
/*! exports provided: UserStore, default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "UserStore", function() { return UserStore; });
/* harmony import */ var nedb_promise__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! nedb-promise */ "nedb-promise");
/* harmony import */ var nedb_promise__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(nedb_promise__WEBPACK_IMPORTED_MODULE_0__);

class UserStore {
  constructor({
    filename,
    autoload
  }) {
    this.store = nedb_promise__WEBPACK_IMPORTED_MODULE_0___default()({
      filename,
      autoload
    });
  }

  async findOne(props) {
    return this.store.findOne(props);
  }

  async insert(user) {
    return this.store.insert(user);
  }

}
/* harmony default export */ __webpack_exports__["default"] = (new UserStore({
  filename: './db/users.json',
  autoload: true
}));

/***/ }),

/***/ "./src/index.js":
/*!**********************!*\
  !*** ./src/index.js ***!
  \**********************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var koa__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! koa */ "koa");
/* harmony import */ var koa__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(koa__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var ws__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ws */ "ws");
/* harmony import */ var ws__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(ws__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var http__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! http */ "http");
/* harmony import */ var http__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(http__WEBPACK_IMPORTED_MODULE_2__);
/* harmony import */ var koa_router__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! koa-router */ "koa-router");
/* harmony import */ var koa_router__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(koa_router__WEBPACK_IMPORTED_MODULE_3__);
/* harmony import */ var koa_bodyparser__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! koa-bodyparser */ "koa-bodyparser");
/* harmony import */ var koa_bodyparser__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(koa_bodyparser__WEBPACK_IMPORTED_MODULE_4__);
/* harmony import */ var _utils__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./utils */ "./src/utils/index.js");
/* harmony import */ var _pet__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ./pet */ "./src/pet/index.js");
/* harmony import */ var _auth__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./auth */ "./src/auth/index.js");
/* harmony import */ var koa_jwt__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! koa-jwt */ "koa-jwt");
/* harmony import */ var koa_jwt__WEBPACK_IMPORTED_MODULE_8___default = /*#__PURE__*/__webpack_require__.n(koa_jwt__WEBPACK_IMPORTED_MODULE_8__);
/* harmony import */ var _koa_cors__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! @koa/cors */ "@koa/cors");
/* harmony import */ var _koa_cors__WEBPACK_IMPORTED_MODULE_9___default = /*#__PURE__*/__webpack_require__.n(_koa_cors__WEBPACK_IMPORTED_MODULE_9__);










const app = new koa__WEBPACK_IMPORTED_MODULE_0___default.a();
const server = http__WEBPACK_IMPORTED_MODULE_2___default.a.createServer(app.callback());
const wss = new ws__WEBPACK_IMPORTED_MODULE_1___default.a.Server({
  server
});
Object(_utils__WEBPACK_IMPORTED_MODULE_5__["initWss"])(wss);
app.use(_koa_cors__WEBPACK_IMPORTED_MODULE_9___default()());
app.use(_utils__WEBPACK_IMPORTED_MODULE_5__["timingLogger"]);
app.use(_utils__WEBPACK_IMPORTED_MODULE_5__["exceptionHandler"]);
app.use(koa_bodyparser__WEBPACK_IMPORTED_MODULE_4___default()());
const prefix = '/api'; // public

const publicApiRouter = new koa_router__WEBPACK_IMPORTED_MODULE_3___default.a({
  prefix
});
publicApiRouter.use('/auth', _auth__WEBPACK_IMPORTED_MODULE_7__["router"].routes());
app.use(publicApiRouter.routes()).use(publicApiRouter.allowedMethods());
app.use(koa_jwt__WEBPACK_IMPORTED_MODULE_8___default()(_utils__WEBPACK_IMPORTED_MODULE_5__["jwtConfig"])); // protected

const protectedApiRouter = new koa_router__WEBPACK_IMPORTED_MODULE_3___default.a({
  prefix
});
protectedApiRouter.use('/pet', _pet__WEBPACK_IMPORTED_MODULE_6__["router"].routes());
app.use(protectedApiRouter.routes()).use(protectedApiRouter.allowedMethods());
server.listen(3000);
console.log('started on port 3000');

/***/ }),

/***/ "./src/pet/index.js":
/*!**************************!*\
  !*** ./src/pet/index.js ***!
  \**************************/
/*! exports provided: router */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _router__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./router */ "./src/pet/router.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "router", function() { return _router__WEBPACK_IMPORTED_MODULE_0__["router"]; });



/***/ }),

/***/ "./src/pet/router.js":
/*!***************************!*\
  !*** ./src/pet/router.js ***!
  \***************************/
/*! exports provided: router */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "router", function() { return router; });
/* harmony import */ var koa_router__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! koa-router */ "koa-router");
/* harmony import */ var koa_router__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(koa_router__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var _store__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./store */ "./src/pet/store.js");
/* harmony import */ var _auth_store__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../auth/store */ "./src/auth/store.js");
/* harmony import */ var _utils__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ../utils */ "./src/utils/index.js");
/* harmony import */ var fs__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! fs */ "fs");
/* harmony import */ var fs__WEBPACK_IMPORTED_MODULE_4___default = /*#__PURE__*/__webpack_require__.n(fs__WEBPACK_IMPORTED_MODULE_4__);





const router = new koa_router__WEBPACK_IMPORTED_MODULE_0___default.a();
router.get('/', async ctx => {
  const response = ctx.response;
  const userId = ctx.state.user._id;
  console.log(userId);
  const page = ctx.query.page;
  let pets = await _store__WEBPACK_IMPORTED_MODULE_1__["default"].find({
    owner: userId
  });
  pets.sort((p1, p2) => {
    if (p1.lastModified > p2.lastModified) {
      return -1;
    } else if (p1.lastModified === p2.lastModified) {
      return 0;
    }

    return 1;
  });
  let lastUpdated = 0; // if (pets.length > 0) {
  //     lastUpdated = pets[0].lastModified;
  //     const ifModifiedSince = ctx.request.get('If-Modified-Since');
  //     if (ifModifiedSince && parseInt(ifModifiedSince) >= lastUpdated) {
  //         ctx.response.status = 304; // NOT MODIFIED
  //         return;
  //     }
  // }

  ctx.response.set('Last-Modified', lastUpdated);
  let result = [];

  if (page !== undefined) {
    for (let i = 0; i < 6; i++) {
      if (page * 6 + i < pets.length) result.push(pets[page * 6 + i]);
    }
  } else {
    result = pets;
  }

  if (result.length > 0) {
    let user = await _auth_store__WEBPACK_IMPORTED_MODULE_2__["default"].findOne({
      _id: result[0].owner
    });

    for (let pet of result) {
      pet.ownerName = user.name; // if (pet.photo) {
      //     pet.photo = {
      //         filepath: pet.photo,
      //         content: fs.readFileSync(`photos/${pet.photo}`, {encoding: 'base64'})
      //     }
      // }
    }
  }

  response.body = result;
  response.status = 200;
});
router.get('/:id', async ctx => {
  const userId = ctx.state.user._id;
  const pet = await _store__WEBPACK_IMPORTED_MODULE_1__["default"].findOne({
    _id: ctx.params.id
  });
  const response = ctx.response;

  if (pet) {
    if (pet.owner === userId) {
      let user = await _auth_store__WEBPACK_IMPORTED_MODULE_2__["default"].findOne({
        _id: pet.owner
      });
      pet.ownerName = user.name;
      response.body = pet;
      response.status = 200; // ok
    } else {
      response.status = 403; // forbidden
    }
  } else {
    response.status = 404; // not found
  }
});

const createPet = async (ctx, pet, response) => {
  try {
    pet.owner = ctx.state.user._id;
    delete pet.ownerName;
    pet.version = 1;
    const oldPhoto = pet.photo;

    if (pet.photo) {
      const imageBuffer = Buffer.from(pet.photo.content, 'base64');
      fs__WEBPACK_IMPORTED_MODULE_4__["writeFile"](`photos/${pet.photo.filepath}`, imageBuffer, function (err) {});
      pet.photo = pet.photo.filepath;
    }

    pet = await _store__WEBPACK_IMPORTED_MODULE_1__["default"].insert(pet);
    let user = await _auth_store__WEBPACK_IMPORTED_MODULE_2__["default"].findOne({
      _id: pet.owner
    });
    pet.ownerName = user.name;

    if (pet.photo) {
      pet.photo = oldPhoto;
    }

    response.body = pet;
    response.status = 201;
    Object(_utils__WEBPACK_IMPORTED_MODULE_3__["broadcast"])(pet.owner, {
      event: 'created',
      payload: response.body
    });
  } catch (err) {
    response.body = {
      message: err.message
    };
    response.status = 400;

    if (pet.photo) {
      fs__WEBPACK_IMPORTED_MODULE_4__["unlink"](`photos/${pet.photo.filepath}`, err => {
        console.log(err);
      });
    }
  }
};

router.post('/', async ctx => await createPet(ctx, ctx.request.body, ctx.response));
router.put('/:id', async ctx => {
  const pet = ctx.request.body;
  const id = ctx.params.id;
  const petId = pet._id;
  const response = ctx.response;

  if (petId && petId !== id) {
    response.body = {
      message: 'Param id and body _id should be the same'
    };
    response.status = 400;
    return;
  }

  if (!petId) {
    await createPet(ctx, pet, response);
  } else {
    const userId = ctx.state.user._id;

    if (userId !== pet.owner) {
      response.body = {
        message: 'Forbidden access to resource'
      };
      response.status = 403;
    } else {
      let ownerName = pet.ownerName;
      delete pet.ownerName;
      const currentPet = await _store__WEBPACK_IMPORTED_MODULE_1__["default"].findOne({
        _id: id
      });
      const currentPhoto = currentPet.photo;

      if (currentPet.version > pet.version) {
        response.status = 409;
      } else {
        pet.version += 1;
        const oldPhoto = pet.photo;

        if (pet.photo) {
          const imageBuffer = Buffer.from(pet.photo.content, 'base64');
          fs__WEBPACK_IMPORTED_MODULE_4__["writeFile"](`photos/${pet.photo.filepath}`, imageBuffer, function (err) {});
          pet.photo = pet.photo.filepath;
        }

        const updatedCount = await _store__WEBPACK_IMPORTED_MODULE_1__["default"].update({
          _id: id
        }, pet);

        if (updatedCount === 1) {
          if (currentPhoto && currentPhoto !== pet.photo) {
            fs__WEBPACK_IMPORTED_MODULE_4__["unlink"](`photos/${currentPhoto}`, err => {
              console.log(err);
            });
          }

          pet.ownerName = ownerName;

          if (oldPhoto) {
            pet.photo = oldPhoto;
          }

          response.body = pet;
          response.status = 200;
          Object(_utils__WEBPACK_IMPORTED_MODULE_3__["broadcast"])(pet.owner, {
            event: 'updated',
            payload: pet
          });
        } else {
          response.body = {
            message: 'Resource no longer exists'
          };
          response.status = 405;

          if (pet.photo) {
            fs__WEBPACK_IMPORTED_MODULE_4__["unlink"](`photos/${pet.photo.filepath}`, () => {});
          }
        }
      }
    }
  }
});
router.del('/:id', async ctx => {
  // const userId = ctx.state.user._id;
  const pet = await _store__WEBPACK_IMPORTED_MODULE_1__["default"].findOne({
    _id: ctx.params.id
  }); // if (pet && userId !== pet.userId) {

  if (pet) {
    await _store__WEBPACK_IMPORTED_MODULE_1__["default"].remove({
      _id: ctx.params.id
    });
    ctx.response.status = 204; // no content

    Object(_utils__WEBPACK_IMPORTED_MODULE_3__["broadcast"])(null, {
      event: 'deleted',
      payload: pet
    });
  }
});

/***/ }),

/***/ "./src/pet/store.js":
/*!**************************!*\
  !*** ./src/pet/store.js ***!
  \**************************/
/*! exports provided: PetStore, default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "PetStore", function() { return PetStore; });
/* harmony import */ var nedb_promise__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! nedb-promise */ "nedb-promise");
/* harmony import */ var nedb_promise__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(nedb_promise__WEBPACK_IMPORTED_MODULE_0__);

class PetStore {
  constructor({
    filename,
    autoload
  }) {
    this.store = nedb_promise__WEBPACK_IMPORTED_MODULE_0___default()({
      filename,
      autoload
    });
  }

  async find(props) {
    return this.store.find(props);
  }

  async findOne(props) {
    return this.store.findOne(props);
  }

  async insert(pet) {
    delete pet._id;
    return this.store.insert(pet);
  }

  async update(props, pet) {
    return this.store.update(props, pet);
  }

  async remove(props) {
    return this.store.remove(props);
  }

}
/* harmony default export */ __webpack_exports__["default"] = (new PetStore({
  filename: './db/pets.json',
  autoload: true
}));

/***/ }),

/***/ "./src/utils/constants.js":
/*!********************************!*\
  !*** ./src/utils/constants.js ***!
  \********************************/
/*! exports provided: jwtConfig */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "jwtConfig", function() { return jwtConfig; });
const jwtConfig = {
  secret: 'my-secret'
};

/***/ }),

/***/ "./src/utils/index.js":
/*!****************************!*\
  !*** ./src/utils/index.js ***!
  \****************************/
/*! exports provided: jwtConfig, exceptionHandler, timingLogger, initWss, broadcast */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _constants__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./constants */ "./src/utils/constants.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "jwtConfig", function() { return _constants__WEBPACK_IMPORTED_MODULE_0__["jwtConfig"]; });

/* harmony import */ var _middlewares__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./middlewares */ "./src/utils/middlewares.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "exceptionHandler", function() { return _middlewares__WEBPACK_IMPORTED_MODULE_1__["exceptionHandler"]; });

/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "timingLogger", function() { return _middlewares__WEBPACK_IMPORTED_MODULE_1__["timingLogger"]; });

/* harmony import */ var _wss__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./wss */ "./src/utils/wss.js");
/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "initWss", function() { return _wss__WEBPACK_IMPORTED_MODULE_2__["initWss"]; });

/* harmony reexport (safe) */ __webpack_require__.d(__webpack_exports__, "broadcast", function() { return _wss__WEBPACK_IMPORTED_MODULE_2__["broadcast"]; });





/***/ }),

/***/ "./src/utils/middlewares.js":
/*!**********************************!*\
  !*** ./src/utils/middlewares.js ***!
  \**********************************/
/*! exports provided: exceptionHandler, timingLogger */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "exceptionHandler", function() { return exceptionHandler; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "timingLogger", function() { return timingLogger; });
const exceptionHandler = async (ctx, next) => {
  try {
    return await next();
  } catch (err) {
    console.log(err);
    ctx.body = {
      message: err.message || 'Unexpected error.'
    };
    ctx.status = err.status || 500;
  }
};
const timingLogger = async (ctx, next) => {
  const start = Date.now();
  await next();
  console.log(`${ctx.method} ${ctx.url} => ${ctx.response.status}, ${Date.now() - start}ms`);
};

/***/ }),

/***/ "./src/utils/wss.js":
/*!**************************!*\
  !*** ./src/utils/wss.js ***!
  \**************************/
/*! exports provided: initWss, broadcast */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "initWss", function() { return initWss; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "broadcast", function() { return broadcast; });
/* harmony import */ var ws__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ws */ "ws");
/* harmony import */ var ws__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(ws__WEBPACK_IMPORTED_MODULE_0__);
/* harmony import */ var jsonwebtoken__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! jsonwebtoken */ "jsonwebtoken");
/* harmony import */ var jsonwebtoken__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(jsonwebtoken__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var _constants__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./constants */ "./src/utils/constants.js");



let wss;
const initWss = value => {
  wss = value;
  wss.on('connection', ws => {
    ws.on('message', message => {
      const {
        type,
        payload: {
          token
        }
      } = JSON.parse(message);

      if (type !== 'authorization') {
        ws.close();
        return;
      }

      try {
        ws.user = jsonwebtoken__WEBPACK_IMPORTED_MODULE_1___default.a.verify(token, _constants__WEBPACK_IMPORTED_MODULE_2__["jwtConfig"].secret);
      } catch (err) {
        ws.close();
      }
    });
  });
};
const broadcast = (userId, data) => {
  if (!wss) {
    return;
  }

  wss.clients.forEach(client => {
    // if (client.readyState === WebSocket.OPEN && userId === client.user._id) {
    if (client.readyState === ws__WEBPACK_IMPORTED_MODULE_0___default.a.OPEN) {
      console.log(`broadcast sent to ${client.user._id}`);
      client.send(JSON.stringify(data));
    }
  });
};

/***/ }),

/***/ 0:
/*!****************************!*\
  !*** multi ./src/index.js ***!
  \****************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(/*! /Users/marialazar/WebstormProjects/vvss_server/src/index.js */"./src/index.js");


/***/ }),

/***/ "@koa/cors":
/*!****************************!*\
  !*** external "@koa/cors" ***!
  \****************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = require("@koa/cors");

/***/ }),

/***/ "fs":
/*!*********************!*\
  !*** external "fs" ***!
  \*********************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = require("fs");

/***/ }),

/***/ "http":
/*!***********************!*\
  !*** external "http" ***!
  \***********************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = require("http");

/***/ }),

/***/ "jsonwebtoken":
/*!*******************************!*\
  !*** external "jsonwebtoken" ***!
  \*******************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = require("jsonwebtoken");

/***/ }),

/***/ "koa":
/*!**********************!*\
  !*** external "koa" ***!
  \**********************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = require("koa");

/***/ }),

/***/ "koa-bodyparser":
/*!*********************************!*\
  !*** external "koa-bodyparser" ***!
  \*********************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = require("koa-bodyparser");

/***/ }),

/***/ "koa-jwt":
/*!**************************!*\
  !*** external "koa-jwt" ***!
  \**************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = require("koa-jwt");

/***/ }),

/***/ "koa-router":
/*!*****************************!*\
  !*** external "koa-router" ***!
  \*****************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = require("koa-router");

/***/ }),

/***/ "nedb-promise":
/*!*******************************!*\
  !*** external "nedb-promise" ***!
  \*******************************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = require("nedb-promise");

/***/ }),

/***/ "ws":
/*!*********************!*\
  !*** external "ws" ***!
  \*********************/
/*! no static exports found */
/***/ (function(module, exports) {

module.exports = require("ws");

/***/ })

/******/ });
//# sourceMappingURL=main.map