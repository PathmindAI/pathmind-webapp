import { storiesOf } from "@storybook/polymer";
import { hljs, hlJava, hlConfig, copyCodeBlock, storyParamConfig } from "../../utils";

import "../../../frontend/src/components/atoms/tag-label.ts";

import code from "./codeSnippets";

hljs.registerLanguage('java', hlJava);

storiesOf('Labels', module)
.addDecorator(storyFn => `<div class="story-wrapper">
    ${storyFn()}
</div>`)
.add('Tag Label', () => `
    <h1>Tag Label</h1>
    <div class="block-wrapper">
        <p>This is a customized component from Pathmind.</p>
        <p>You can add class names directly to the component to style it.</p>
        <p>At the moment, this component only accepts text.</p>
    </div>
    <h2>Customization</h2>
    <div class="block-wrapper">
        <div class="col-wrapper">
            <tag-label text="Archived"></tag-label>
            ${copyCodeBlock(code.tagLabel, hlConfig)}
        </div>
        <div class="col-wrapper">
            <tag-label text="Draft"></tag-label>
            ${copyCodeBlock(code.tagLabelSetText, hlConfig)}
        </div>
        <div class="col-wrapper">
            The label is hidden when there is no text.
            <tag-label></tag-label>
            ${copyCodeBlock(code.tagLabelEmpty, hlConfig)}
        </div>
        <div class="col-wrapper">
            <tag-label outline="true" text="Archived"></tag-label>
            ${copyCodeBlock(code.tagLabelOutline, hlConfig)}
        </div>
        <div class="col-wrapper">
            <tag-label outline="true" size="small" text="Archived"></tag-label>
            ${copyCodeBlock(code.tagLabelOutlineSmall, hlConfig)}
        </div>
        <div class="col-wrapper">
            <tag-label size="small" text="Archived"></tag-label>
            ${copyCodeBlock(code.tagLabelSmall, hlConfig)}
        </div>
        <div class="col-wrapper">
            <tag-label class="error-label" size="small" text="Archived"></tag-label>
            ${copyCodeBlock(code.tagLabelSmallError, hlConfig)}
        </div>
    </div>
`, storyParamConfig);