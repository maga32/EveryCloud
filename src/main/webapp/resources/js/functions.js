/** alert reject reason and move cursor
 *
 * @param {string}pointer cursor to input id
 * @param {string}reason alert reject reason
 * @returns {boolean} False
 */
function reject(pointer, reason) {
    alert(reason);
    $("#"+pointer).focus();
    return false;
}

/** translate filesize
 *
 * @param {number}size filesize(byte)
 * @returns {string} xx gb(mb,kb,byte)
 */
function fileSize(size) {
    if(size > 1073741824) {
        return (size/1073741824).toFixed(2) + "gb";
    } else if(size > 1048576) {
        return (size/1048576).toFixed(1) + "mb";
    } else if(size > 1024) {
        return (size/1024).toFixed(1) + "kb";
    } else {
        return size + "byte";
    }
}