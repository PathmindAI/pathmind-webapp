export default {
tagLabel: 
`TagLabel yourLabel = new TagLabel("Archived");`,

tagLabelEmpty: 
`TagLabel yourLabel = new TagLabel("");`,

tagLabelSetText: 
`TagLabel yourLabel = new TagLabel("");
yourLabel.setText("Draft");`,

tagLabelOutline: 
`TagLabel yourLabel = new TagLabel("Archived", true);`,

tagLabelOutlineSmall: 
`TagLabel yourLabel = new TagLabel("Archived", true, "small");`,

tagLabelSmall: 
`TagLabel yourLabel = new TagLabel("Archived", false, "small");`,

tagLabelSmallError: 
`TagLabel yourLabel = new TagLabel("Archived", false, "small");
yourLabel.addClassName("error-label");`,
};