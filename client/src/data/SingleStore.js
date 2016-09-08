import ItemStore from './ItemStore.js';

/**
 * Adds value-specific logic for single lines.
 */
class SingleStore extends ItemStore {
  constructor(serverBase: String) {
    super(serverBase: String);
  }

  addValues(id: string, values: Object, callback: Function) {
    window.$.ajax({
      type: "POST",
      url: `${this.serverBase}/${id}/values`,
      dataType: 'json',
      data: JSON.stringify(values),
    }).done(data => {
      // TODO - clear out cache.
      callback(data);
    }).fail(() => {
      alert("Oops, cant create...");
    });
  }
}

export default SingleStore;
