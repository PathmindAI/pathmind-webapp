export default {
primaryButton: 
`Button yourButton = new Button("Primary", evt -> doSomething());
yourButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);`,

secondaryButton: 
`Button yourButton = new Button("Secondary", evt -> doSomething());`,

tertiaryButton: 
`Button yourButton = new Button("Tertiary", evt -> doSomething());
yourButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);`,

tertiaryInlineButton: 
`Button yourButton = new Button("Tertiary Inline", evt -> doSomething());
yourButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);`,

actionButton: 
`Button archiveExperimentButton = new Button(VaadinIcon.ARCHIVE.create(), evt -> doSomething());
archiveExperimentButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
archiveExperimentButton.addClassName("action-button");`,

iconButton: 
`Button archiveExperimentButton = new Button(VaadinIcon.ARCHIVE.create(), evt -> doSomething());
archiveExperimentButton.addThemeVariants(ButtonVariant.LUMO_ICON);`,

primarySuccessButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);`,

secondarySuccessButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);`,

tertiarySuccessButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);`,

tertiaryInlineSuccessButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_SUCCESS);`,

iconSuccessButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SUCCESS);`,

primaryErrorButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);`,

secondaryErrorButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_ERROR);`,

tertiaryErrorButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);`,

tertiaryInlineErrorButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);`,

iconErrorButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);`,

largeButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);`,

primaryNormalButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);`,

primarySmallButton: 
`yourButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);`,
};