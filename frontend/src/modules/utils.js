/** reset Object
 *
 * @param object
 * @param deep
 */
const initObject = (object, deep) => {
  let self = this
  self.obj = object

  for (let key in object) {
    if (object [key] === null) {
      self.obj[key] = null
    } else {
      switch (typeof object[key]) {
        case 'number':          // 정수
          self.obj[key] = 0
          break
        case 'boolean':         // boolean
          self.obj[key] = false
          break
        case 'array':           // array
          self.obj[key] = []
          break
        case 'object':          // obiect
          if (deep) {
            initObject(self.obj[key])
          } else {
            self.obj[key] = {}
          }
          break
        default:
          self.obj[key] = ''    // 문자
      }
    }
  }
}

/** add comma to number
 *
 * @param value ex)1234567.89
 * @returns {string} ex)1,234,567.89
 */
const addComma = (value) => {
  return value.toString().trim().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")
}

/** determine number is integer
 *
 * @param number
 * @returns {boolean}
 */
const isInteger = (number) => {
  return number % 1 === 0
}

/** If String doesn't end with '/', add '/' end of the String
 *
 * @param str
 * @returns String
 */
const addSlash = (str) => {
  return str.endsWith('/') ? str : str+'/'
}

/** If String end with '/', remove last '/' in the String
 *
 * @param str
 * @returns String
 */
const removeSlash = (str) => {
  return str.endsWith('/') ? str.slice(0, -1) : str
}

/** translate filesize
 *
 * @param {number}size filesize(byte)
 * @returns {string} xx gb(mb,kb,byte)
 */
const fileSize = (size) => {
  const divSize = 1024

  if(size > divSize ** 3) {
    return (size/divSize ** 3).toFixed(2) + "gb"
  } else if(size > divSize ** 2) {
    return (size/divSize ** 2).toFixed(1) + "mb"
  } else if(size > divSize) {
    return (size/divSize).toFixed(1) + "kb"
  } else {
    return size + "byte"
  }
}

export default {
  initObject,
  addComma,
  isInteger,
  fileSize,
  addSlash,
  removeSlash,
}