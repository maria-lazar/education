import dataStore from 'nedb-promise';


export class PetStore {
    constructor({filename, autoload}) {
        this.store = dataStore({filename, autoload});
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
    };

    async update(props, pet) {
        return this.store.update(props, pet);
    }

    async remove(props) {
        return this.store.remove(props);
    }
}

export default new PetStore({filename: './db/pets.json', autoload: true});