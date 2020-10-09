package cmd;

/**
 * Models the operations that could be performed by the tool
 */
public enum Operation {

  ADD_ENTRY,
  COMPLETE_ENTRY,
  DISPLAY,

  // Sub commands under DISPLAY_ENTRY
  SHOW_INCOMPLETE,
  SHOW_CATEGORY,
  SORT_BY_DATE,
  SORT_BY_PRIORITY
}
