/**
 * 待修改
 */
const directives = {
  has: {
    bind: function (el, binding) {
      if (!Vue.prototype.$_has(binding.value)) {
        if (el.parentNode === null || el.parentNode === undefined) {
          el.hidden = true;
        } else {
          el.parentNode.removeChild(el);
        }
      }
    }
  }
}

export default directives
