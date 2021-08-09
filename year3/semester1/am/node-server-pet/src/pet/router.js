import Router from 'koa-router';
import petStore from './store';
import userStore from '../auth/store';
import {broadcast} from "../utils";
import * as fs from "fs";

export const router = new Router();

router.get('/', async (ctx) => {
    const response = ctx.response;
    const userId = ctx.state.user._id;
    console.log(userId);
    const page = ctx.query.page;
    let pets = await petStore.find({owner: userId});
    pets.sort((p1, p2) => {
        if (p1.lastModified > p2.lastModified) {
            return -1;
        } else if (p1.lastModified === p2.lastModified) {
            return 0;
        }
        return 1;
    })
    let lastUpdated = 0;
    // if (pets.length > 0) {
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
            if (page * 6 + i < pets.length)
                result.push(pets[page * 6 + i]);
        }
    } else {
        result = pets;
    }
    if (result.length > 0) {
        let user = await userStore.findOne({_id: result[0].owner});
        for (let pet of result) {
            pet.ownerName = user.name;
            if (pet.photo) {
                pet.photo = {
                    filepath: pet.photo,
                    content: fs.readFileSync(`photos/${pet.photo}`, {encoding: 'base64'})
                }
            }
        }
    }
    response.body = result;
    response.status = 200;
});

router.get('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const pet = await petStore.findOne({_id: ctx.params.id});
    const response = ctx.response;
    if (pet) {
        if (pet.owner === userId) {
            let user = await userStore.findOne({_id: pet.owner});
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
            fs.writeFile(`photos/${pet.photo.filepath}`, imageBuffer, function (err) {

            });
            pet.photo = pet.photo.filepath;
        }
        pet = await petStore.insert(pet);
        let user = await userStore.findOne({_id: pet.owner});
        pet.ownerName = user.name;
        if (pet.photo) {
            pet.photo = oldPhoto;
        }
        response.body = pet;
        response.status = 201;
        broadcast(pet.owner, {event: 'created', payload: response.body});
    } catch (err) {
        response.body = {message: err.message};
        response.status = 400;
        if (pet.photo) {
            fs.unlink(`photos/${pet.photo.filepath}`, (err) => {
                console.log(err);
            });
        }
    }
};

router.post('/', async ctx => await createPet(ctx, ctx.request.body, ctx.response));

router.put('/:id', async (ctx) => {
        const pet = ctx.request.body;
        const id = ctx.params.id;
        const petId = pet._id;
        const response = ctx.response;
        if (petId && petId !== id) {
            response.body = {message: 'Param id and body _id should be the same'};
            response.status = 400;
            return;
        }
        if (!petId) {
            await createPet(ctx, pet, response);
        } else {
            const userId = ctx.state.user._id;
            if (userId !== pet.owner) {
                response.body = {message: 'Forbidden access to resource'};
                response.status = 403;
            } else {
                let ownerName = pet.ownerName;
                delete pet.ownerName;
                const currentPet = await petStore.findOne({_id: id});
                const currentPhoto = currentPet.photo;
                if (currentPet.version > pet.version) {
                    response.status = 409;
                } else {
                    pet.version += 1;
                    const oldPhoto = pet.photo;
                    if (pet.photo) {
                        const imageBuffer = Buffer.from(pet.photo.content, 'base64');
                        fs.writeFile(`photos/${pet.photo.filepath}`, imageBuffer, function (err) {

                        });
                        pet.photo = pet.photo.filepath;
                    }
                    const updatedCount = await petStore.update({_id: id}, pet);
                    if (updatedCount === 1) {
                        if (currentPhoto && currentPhoto !== pet.photo) {
                            fs.unlink(`photos/${currentPhoto}`, (err) => {
                                console.log(err);
                            });
                        }
                        pet.ownerName = ownerName;
                        if (oldPhoto) {
                            pet.photo = oldPhoto;
                        }
                        response.body = pet;
                        response.status = 200;
                        broadcast(pet.owner, {event: 'updated', payload: pet});
                    } else {
                        response.body = {message: 'Resource no longer exists'};
                        response.status = 405;
                        if (pet.photo) {
                            fs.unlink(`photos/${pet.photo.filepath}`, () => {
                            });
                        }
                    }
                }
            }
        }
    }
);

router.del('/:id', async (ctx) => {
    const userId = ctx.state.user._id;
    const note = await petStore.findOne({_id: ctx.params.id});
    if (note && userId !== note.userId) {
        ctx.response.status = 403; // forbidden
    } else {
        await petStore.remove({_id: ctx.params.id});
        ctx.response.status = 204; // no content
    }
});
